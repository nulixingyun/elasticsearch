/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License;
 * you may not use this file except in compliance with the Elastic License.
 */
package org.elasticsearch.xpack.sql.expression;

import org.elasticsearch.xpack.sql.capabilities.UnresolvedException;
import org.elasticsearch.xpack.sql.tree.Location;
import org.elasticsearch.xpack.sql.tree.NodeInfo;
import java.util.Objects;

import static java.util.Collections.singletonList;

import java.util.List;

public class UnresolvedAlias extends UnresolvedNamedExpression {

    private final Expression child;

    public UnresolvedAlias(Location location, Expression child) {
        super(location, singletonList(child));
        this.child = child;
    }

    @Override
    protected NodeInfo<UnresolvedAlias> info() {
        return NodeInfo.create(this, UnresolvedAlias::new, child);
    }

    @Override
    public Expression replaceChildren(List<Expression> newChildren) {
        if (newChildren.size() != 1) {
            throw new IllegalArgumentException("expected [1] child but received [" + newChildren.size() + "]");
        }
        return new UnresolvedAlias(location(), newChildren.get(0));
    }

    public Expression child() {
        return child;
    }

    @Override
    public String unresolvedMessage() {
        return "Unknown alias [" + name() + "]";
    }

    @Override
    public boolean nullable() {
        throw new UnresolvedException("nullable", this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(child);
    }

    @Override
    public boolean equals(Object obj) {
        /*
         * Intentionally not calling the superclass
         * equals because it uses id which we always
         * mutate when we make a clone.
         */
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        return Objects.equals(child, ((UnresolvedAlias) obj).child);
    }

    @Override
    public String toString() {
        return child + " AS ?";
    }
}
