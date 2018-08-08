/*
 * Copyright 2015-2018 the original author or authors.
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
package javax.application.resources.tck;

import org.assertj.core.api.JUnitSoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.application.resources.NoSuchResourceException;
import javax.application.resources.ResourceResolver;
import java.util.Locale;

/**
 * @author Andres Almiray
 */
public abstract class ResourceResolverTest {
    protected static final Object[] TWO_ARGS = new Object[]{"apple", "doctor"};
    protected static final String DEFAULT_VALUE = "not found";
    protected static final String KEY_PROVERB = "key.proverb";
    protected static final String KEY_PROVERB_BOGUS = "key.proverb.bogus";
    protected static final String KEY_BOGUS = "key.bogus";
    protected static final String KEY_INTEGER = "key.integer";
    protected static final String PROVERB_FORMAT = "An {0} a day keeps the {1} away";
    protected static final String PROVERB_TEXT = "An apple a day keeps the doctor away";

    @Rule
    public final JUnitSoftAssertions t = new JUnitSoftAssertions();

    protected abstract ResourceResolver resolveResourceResolver();

    private Locale defaultLocale;

    @Before
    public void setup() {
        defaultLocale = Locale.getDefault();
        Locale.setDefault(Locale.ENGLISH);
    }

    @After
    public void cleanup() {
        Locale.setDefault(defaultLocale);
    }

    @Test
    public void verify_resolveResource_withArguments_withLocale() {
        // given:
        ResourceResolver resourceResolver = resolveResourceResolver();

        // expect:
        t.assertThat((Object) resourceResolver.resolveResource(KEY_PROVERB))
            .isEqualTo(PROVERB_FORMAT);
        t.assertThat((Object) resourceResolver.resolveResource(KEY_PROVERB, Locale.getDefault()))
            .isEqualTo(PROVERB_FORMAT);
        t.assertThat((Object) resourceResolver.resolveResource(KEY_PROVERB, TWO_ARGS))
            .isEqualTo(PROVERB_TEXT);
        t.assertThat((Object) resourceResolver.resolveResource(KEY_PROVERB, TWO_ARGS, Locale.getDefault()))
            .isEqualTo(PROVERB_TEXT);
    }

    @Test
    public void verify_resolveResource_withArguments_withLocale_withDefaultValue() {
        // given:
        ResourceResolver resourceResolver = resolveResourceResolver();

        // expect:
        t.assertThat(resourceResolver.resolveResource(KEY_PROVERB_BOGUS, DEFAULT_VALUE))
            .isEqualTo(DEFAULT_VALUE);
        t.assertThat(resourceResolver.resolveResource(KEY_PROVERB_BOGUS, Locale.getDefault(), DEFAULT_VALUE))
            .isEqualTo(DEFAULT_VALUE);
        t.assertThat(resourceResolver.resolveResource(KEY_PROVERB_BOGUS, TWO_ARGS, DEFAULT_VALUE))
            .isEqualTo(DEFAULT_VALUE);
        t.assertThat(resourceResolver.resolveResource(KEY_PROVERB_BOGUS, TWO_ARGS, Locale.getDefault(), DEFAULT_VALUE))
            .isEqualTo(DEFAULT_VALUE);
    }

    @Test
    public void verify_resolveResourceConverted_withArguments_withLocale() {
        // given:
        ResourceResolver resourceResolver = resolveResourceResolver();

        // expect:
        int value = 42;
        t.assertThat(resourceResolver.resolveResourceConverted(KEY_INTEGER, Integer.class))
            .isEqualTo(value);
        t.assertThat(resourceResolver.resolveResourceConverted(KEY_INTEGER, Locale.getDefault(), Integer.class))
            .isEqualTo(value);
        t.assertThat(resourceResolver.resolveResourceConverted(KEY_INTEGER, TWO_ARGS, Integer.class))
            .isEqualTo(value);
        t.assertThat(resourceResolver.resolveResourceConverted(KEY_INTEGER, TWO_ARGS, Locale.getDefault(), Integer.class))
            .isEqualTo(value);
    }

    @Test
    public void verify_resolveResourceConverted_withArguments_withLocale_withDefaultValue() {
        // given:
        ResourceResolver resourceResolver = resolveResourceResolver();

        // expect:
        int defaultValue = 21;
        t.assertThat(resourceResolver.resolveResourceConverted(KEY_BOGUS, defaultValue, Integer.class))
            .isEqualTo(defaultValue);
        t.assertThat(resourceResolver.resolveResourceConverted(KEY_BOGUS, Locale.getDefault(), defaultValue, Integer.class))
            .isEqualTo(defaultValue);
        t.assertThat(resourceResolver.resolveResourceConverted(KEY_BOGUS, TWO_ARGS, defaultValue, Integer.class))
            .isEqualTo(defaultValue);
        t.assertThat(resourceResolver.resolveResourceConverted(KEY_BOGUS, TWO_ARGS, Locale.getDefault(), defaultValue, Integer.class))
            .isEqualTo(defaultValue);
    }

    @Test
    public void verify_resolveResource_withUnknownKey_withArguments_withLocale() {
        // given:
        ResourceResolver resourceResolver = resolveResourceResolver();

        // expect:
        t.assertThatThrownBy(() -> resourceResolver.resolveResource(KEY_BOGUS))
            .isInstanceOf(NoSuchResourceException.class);
        t.assertThatThrownBy(() -> resourceResolver.resolveResource(KEY_BOGUS, Locale.getDefault()))
            .isInstanceOf(NoSuchResourceException.class);
        t.assertThatThrownBy(() -> resourceResolver.resolveResource(KEY_BOGUS, TWO_ARGS))
            .isInstanceOf(NoSuchResourceException.class);
        t.assertThatThrownBy(() -> resourceResolver.resolveResource(KEY_BOGUS, TWO_ARGS, Locale.getDefault()))
            .isInstanceOf(NoSuchResourceException.class);
    }

    @Test
    public void verify_resolveResourceConverted_withUnknownKey_withArguments_withLocale() {
        // given:
        ResourceResolver resourceResolver = resolveResourceResolver();

        // expect:
        t.assertThatThrownBy(() -> resourceResolver.resolveResourceConverted(KEY_BOGUS, Integer.class))
            .isInstanceOf(NoSuchResourceException.class);
        t.assertThatThrownBy(() -> resourceResolver.resolveResourceConverted(KEY_BOGUS, Locale.getDefault(), Integer.class))
            .isInstanceOf(NoSuchResourceException.class);
        t.assertThatThrownBy(() -> resourceResolver.resolveResourceConverted(KEY_BOGUS, TWO_ARGS, Integer.class))
            .isInstanceOf(NoSuchResourceException.class);
        t.assertThatThrownBy(() -> resourceResolver.resolveResourceConverted(KEY_BOGUS, TWO_ARGS, Locale.getDefault(), Integer.class))
            .isInstanceOf(NoSuchResourceException.class);
    }
}
