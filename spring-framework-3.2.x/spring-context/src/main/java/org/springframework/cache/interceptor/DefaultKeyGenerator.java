/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cache.interceptor;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Default key generator. Returns {@value #NO_PARAM_KEY} if no
 * parameters are provided, the parameter itself if only one is given or
 * a hash code computed from all given parameters' hash code values.
 * Uses the constant value {@value #NULL_PARAM_KEY} for any
 * {@code null} parameters given.
 *
 * @author Costin Leau
 * @author Chris Beams
 * @author Juergen Hoeller
 * @since 3.1
 */
public class DefaultKeyGenerator implements KeyGenerator {

	public static final int NO_PARAM_KEY = 0;

	public static final int NULL_PARAM_KEY = 53;

	public Object generate(Object target, Method method, Object... params) {
		if (params.length == 0) {
			return NO_PARAM_KEY;
		}
		if (params.length == 1) {
			Object param = params[0];
			if (param == null) {
				return NULL_PARAM_KEY;
			}
			if (!param.getClass().isArray()) {
				return param;
			}
		}
		return Arrays.deepHashCode(params);
	}

}
