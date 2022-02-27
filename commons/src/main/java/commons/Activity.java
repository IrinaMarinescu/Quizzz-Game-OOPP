package commons;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Entity class for activities. Spring will automatically create the DB table based on the class fields.
 */
@Entity
public class Activity {
    /**
     * Primary key as a string. Should comply with the format they give in the activity bank for the ID.
     *
     * @see <a href="https://gitlab.ewi.tudelft.nl/cse1105/2021-2022/activity-bank">OOPP Activity Bank</a>
     */
    @Id
    @NotNull(message = "Activity ID should not be nll!")
    public String id;

    /**
     * TODO: add - implementation 'org.springframework.boot:spring-boot-starter-validation:2.4.0' - in build.gradle if you haven't already.
     * The @NotNull annotation does not let this values be initialised as null.
     */
    @NotNull(message = "Activity title should not be null!")
    public String title;

    @NotNull(message = "Activity image path should not be null!")
    public String imagePath;

    public int consumptionInWh;

    @NotNull(message = "Activity source should not be null!")
    public String source;

    @SuppressWarnings("unused")
    private Activity() {
        // for object mappers.
    }

    public Activity(String id, String imagePath, String title, int consumptionInWh, String source) {
        this.id = id;
        this.imagePath = imagePath;
        this.title = title;
        this.consumptionInWh = consumptionInWh;
        this.source = source;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Activity) && EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }
}
