package org.apache.commons.math4.geometry.enclosing;

import org.apache.commons.math4.geometry.Point;
import org.apache.commons.math4.geometry.Space;

public interface Encloser<S extends Space, P extends Point<S>> {
    EnclosingBall<S, P> enclose(Iterable<P> iterable);
}
