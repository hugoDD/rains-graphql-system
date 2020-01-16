/*
 *  com.github.rutledgepaulv.rqe.testsupport.Comment
 *  *
 *  * Copyright (C) 2016 Paul Rutledge <paul.v.rutledge@gmail.com>
 *  *
 *  * This software may be modified and distributed under the terms
 *  * of the MIT license.  See the LICENSE file for details.
 *
 */

package rsql.testsupport;

import java.time.Instant;
import java.util.Objects;

public class Comment {

    private String comment;
    private Instant timestamp;

    public String getComment() {
        return comment;
    }

    public Comment setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public Comment setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Comment)) {
            return false;
        }
        Comment comment1 = (Comment) o;
        return Objects.equals(comment, comment1.comment) && Objects.equals(timestamp, comment1.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(comment, timestamp);
    }

    @Override
    public String toString() {
        return "Comment{" +
                "comment='" + comment + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
