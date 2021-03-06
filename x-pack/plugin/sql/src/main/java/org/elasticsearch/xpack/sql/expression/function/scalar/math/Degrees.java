/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License;
 * you may not use this file except in compliance with the Elastic License.
 */
package org.elasticsearch.xpack.sql.expression.function.scalar.math;

import org.elasticsearch.xpack.sql.expression.Expression;
import org.elasticsearch.xpack.sql.expression.function.scalar.math.MathProcessor.MathOperation;
import org.elasticsearch.xpack.sql.tree.Location;
import org.elasticsearch.xpack.sql.tree.NodeInfo;

/**
 * Convert from <a href="https://en.wikipedia.org/wiki/Radian">radians</a>
 * to <a href="https://en.wikipedia.org/wiki/Degree_(angle)">degrees</a>.
 */
public class Degrees extends MathFunction {
    public Degrees(Location location, Expression field) {
        super(location, field);
    }

    @Override
    protected NodeInfo<Degrees> info() {
        return NodeInfo.create(this, Degrees::new, field());
    }

    @Override
    protected Degrees replaceChild(Expression newChild) {
        return new Degrees(location(), newChild);
    }

    @Override
    protected String mathFunction() {
        return "toDegrees";
    }

    @Override
    protected MathOperation operation() {
        return MathOperation.DEGREES;
    }
}
