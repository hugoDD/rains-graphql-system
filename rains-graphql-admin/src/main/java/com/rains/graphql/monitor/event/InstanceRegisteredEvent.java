package com.rains.graphql.monitor.event;
/*
 * Copyright 2014-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import com.rains.graphql.monitor.model.Registration;

import java.time.Instant;

/**
 * This event gets emitted when an instance is registered.
 *
 * @author Johannes Edmeier
 */
@lombok.Data
@lombok.EqualsAndHashCode(callSuper = true)
@lombok.ToString(callSuper = true)
public class InstanceRegisteredEvent extends InstanceEvent {
	private static final long serialVersionUID = 1L;
	private final Registration registration;

	public InstanceRegisteredEvent(String instance, long version, Registration registration) {
		this(instance, version, Instant.now(), registration);
	}

	public InstanceRegisteredEvent(String instance, long version, Instant timestamp, Registration registration) {
		super(instance, version, "REGISTERED", timestamp);
		this.registration = registration;
	}
}
