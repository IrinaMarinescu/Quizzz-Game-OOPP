package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

/**
 * Entity class for activities. Spring will automatically create the DB table based on the class fields.
 */
@Entity
public class Activity {

    /**
     * GenerationType.IDENTITY is used for auto-increment fields. So, the first activity will have ID 1, the second 2 and so on.
     * TODO: H2 has a cache of size 32 when inserting autoincrement fields. So for example, if we insert 2 activities, they will have ids 1 and 2,
     * but after restarting Spring, the next ones will start from 32
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    /**
     * TODO: add - implementation 'org.springframework.boot:spring-boot-starter-validation:2.4.0' - in build.gradle if you haven't already.
     * The @NotNull annotation does not let this values be initialised as null.
     */
    @NotNull(message = "Activity title should not be null!")
    public String title;

    public int consumptionInWh;

    @NotNull(message = "Activity source should not be null!")
    public String source;

    @SuppressWarnings("unused")
    private Activity() {
        // for object mappers.
    }

    public Activity(String title, int consumptionInWh, String source) {
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
