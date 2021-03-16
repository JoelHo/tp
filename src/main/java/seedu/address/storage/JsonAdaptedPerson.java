package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.human.Name;
import seedu.address.model.human.Phone;
import seedu.address.model.human.driver.Driver;
import seedu.address.model.human.person.Address;
import seedu.address.model.human.person.Person;
import seedu.address.model.human.person.TripDay;
import seedu.address.model.human.person.TripTime;
import seedu.address.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Person}.
 */
class JsonAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";

    private final String name;
    private final String phone;
    private final String address;
    private final String tripDay;
    private final String tripTime;
    private final String driver;
    private final List<JsonAdaptedTag> tagged = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedPerson} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedPerson(@JsonProperty("name") String name, @JsonProperty("phone") String phone,
                             @JsonProperty("address") String address, @JsonProperty("tripDay") String tripDay,
                             @JsonProperty("tripTime") String tripTime, @JsonProperty("driver") String driver,
                             @JsonProperty("tagged") List<JsonAdaptedTag> tagged) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.tripDay = tripDay;
        this.tripTime = tripTime;
        this.driver = driver;
        if (tagged != null) {
            this.tagged.addAll(tagged);
        }
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedPerson(Person source) {
        name = source.getName().fullName;
        phone = source.getPhone().value;
        address = source.getAddress().value;
        tripDay = source.getTripDay().value.toString();
        tripTime = source.getTripTime().value;
        driver = source.getDriverStr();
        tagged.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Person} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Person toModelType() throws IllegalValueException {
        final List<Tag> personTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tagged) {
            personTags.add(tag.toModelType());
        }

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        final Phone modelPhone = new Phone(phone);

        if (address == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName()));
        }
        if (!Address.isValidAddress(address)) {
            throw new IllegalValueException(Address.MESSAGE_CONSTRAINTS);
        }
        final Address modelAddress = new Address(address);

        if (tripDay == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, TripDay.class.getSimpleName()));
        }
        if (!TripDay.isValidTripDay(tripDay)) {
            throw new IllegalValueException(TripDay.MESSAGE_CONSTRAINTS);
        }
        final TripDay modelTripDay = new TripDay(tripDay);

        if (tripTime == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    TripTime.class.getSimpleName()));
        }
        if (!TripTime.isValidTripTime(tripTime)) {
            throw new IllegalValueException(TripTime.MESSAGE_CONSTRAINTS);
        }
        final TripTime modelTripTime = new TripTime(tripTime);

        final Set<Tag> modelTags = new HashSet<>(personTags);
        if (Driver.isValidDriver(driver)) {
            final Driver modelDriver = new Driver(driver);
            return new Person(modelName, modelPhone, modelAddress, modelTripDay, modelTripTime, modelDriver, modelTags);
        } else {
            return new Person(modelName, modelPhone, modelAddress, modelTripDay, modelTripTime, modelTags);
        }
    }

}