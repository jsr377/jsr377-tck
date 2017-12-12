/*
 * Copyright 2015-2017 the original author or authors.
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
package javax.application.resources;

import org.assertj.core.api.JUnitSoftAssertions;
import org.junit.Rule;
import org.junit.Test;

/**
 * Verifies that implementations of {@code ResourceInjector} can inject resources on a target bean.
 * The following key/value pairs must be resolvable
 * <p>
 * <pre>
 * javax.application.resources.ResourceInjectorTest.Bean.privateField=privateField
 * javax.application.resources.ResourceInjectorTest.Bean.value=fieldBySetter
 * javax.application.resources.ResourceInjectorTest.Bean.privateIntField=42
 * javax.application.resources.ResourceInjectorTest.Bean.intValue=21
 * sample.key.no_args=no_args
 * sample.key.with_args=with_args {0} {1}
 * javax.application.resources.ResourceInjectorTest.SuperBean.superPrivateField=superPrivateField
 * javax.application.resources.ResourceInjectorTest.SuperBean.superValue=superFieldBySetter
 * javax.application.resources.ResourceInjectorTest.SuperBean.superPrivateIntField=420
 * javax.application.resources.ResourceInjectorTest.SuperBean.superIntValue=210
 * sample.super.key.no_args=super_no_args
 * sample.super.key.with_args=super_with_args {0} {1}
 * </pre>
 *
 * @author Andres Almiray
 */
public abstract class ResourceInjectorTest {
    @Rule
    public final JUnitSoftAssertions t = new JUnitSoftAssertions();

    protected abstract ResourceInjector resolveResourcesInjector();

    @Test
    public void injectResourcesOnBean() {
        // given:
        Bean bean = new Bean();

        // when:
        resolveResourcesInjector().injectResources(bean);

        // then:
        t.assertThat(bean.privateField()).isEqualTo("privateField");
        t.assertThat(bean.fieldBySetter()).isEqualTo("fieldBySetter");
        t.assertThat(bean.privateIntField()).isEqualTo(42);
        t.assertThat(bean.intFieldBySetter()).isEqualTo(21);
        t.assertThat(bean.fieldWithKey()).isEqualTo("no_args");
        t.assertThat(bean.fieldWithKeyAndArgs()).isEqualTo("with_args 1 2");
        t.assertThat(bean.fieldWithKeyNoArgsWithDefault()).isEqualTo("DEFAULT_NO_ARGS");
        t.assertThat(bean.fieldWithKeyWithArgsWithDefault()).isEqualTo("DEFAULT_WITH_ARGS");
        t.assertThat(bean.notFound()).isNullOrEmpty();
        t.assertThat(bean.superPrivateField()).isEqualTo("superPrivateField");
        t.assertThat(bean.superFieldBySetter()).isEqualTo("superFieldBySetter");
        t.assertThat(bean.superPrivateIntField()).isEqualTo(420);
        t.assertThat(bean.superIntFieldBySetter()).isEqualTo(210);
        t.assertThat(bean.superFieldWithKey()).isEqualTo("super_no_args");
        t.assertThat(bean.superFieldWithKeyAndArgs()).isEqualTo("super_with_args 1 2");
        t.assertThat(bean.superFieldWithKeyNoArgsWithDefault()).isEqualTo("SUPER_DEFAULT_NO_ARGS");
        t.assertThat(bean.superFieldWithKeyWithArgsWithDefault()).isEqualTo("SUPER_DEFAULT_WITH_ARGS");
        t.assertThat(bean.superNotFound()).isNullOrEmpty();
    }

    public static class SuperBean {
        @InjectedResource
        private String superPrivateField;

        private String superFieldBySetter;

        @InjectedResource
        private int superPrivateIntField;

        private int superIntFieldBySetter;

        @InjectedResource
        public void setSuperValue(String value) {
            this.superFieldBySetter = value;
        }

        @InjectedResource
        public void setSuperIntValue(int value) {
            this.superIntFieldBySetter = value;
        }

        @InjectedResource(value = "sample.super.key.no_args")
        private String superFieldWithKey;

        @InjectedResource(value = "sample.super.key.with_args", args = {"1", "2"})
        private String superFieldWithKeyAndArgs;

        @InjectedResource(value = "sample.super.key.no_args.with_default", defaultValue = "SUPER_DEFAULT_NO_ARGS")
        private String superFieldWithKeyNoArgsWithDefault;

        @InjectedResource(value = "sample.super.key.no_args.with_default", args = {"1", "2"}, defaultValue = "SUPER_DEFAULT_WITH_ARGS")
        private String superFieldWithKeyWithArgsWithDefault;

        @InjectedResource
        private String superNotFound;

        public String superPrivateField() {
            return superPrivateField;
        }

        public String superFieldBySetter() {
            return superFieldBySetter;
        }

        public int superPrivateIntField() {
            return superPrivateIntField;
        }

        public int superIntFieldBySetter() {
            return superIntFieldBySetter;
        }

        public String superFieldWithKey() {
            return superFieldWithKey;
        }

        public String superFieldWithKeyAndArgs() {
            return superFieldWithKeyAndArgs;
        }

        public String superFieldWithKeyNoArgsWithDefault() {
            return superFieldWithKeyNoArgsWithDefault;
        }

        public String superFieldWithKeyWithArgsWithDefault() {
            return superFieldWithKeyWithArgsWithDefault;
        }

        public String superNotFound() {
            return superNotFound;
        }
    }

    public static class Bean extends SuperBean {
        @InjectedResource
        private String privateField;

        private String fieldBySetter;

        @InjectedResource
        private int privateIntField;

        private int intFieldBySetter;

        @InjectedResource
        public void setValue(String value) {
            this.fieldBySetter = value;
        }

        @InjectedResource
        public void setIntValue(int value) {
            this.intFieldBySetter = value;
        }

        @InjectedResource(value = "sample.key.no_args")
        private String fieldWithKey;

        @InjectedResource(value = "sample.key.with_args", args = {"1", "2"})
        private String fieldWithKeyAndArgs;

        @InjectedResource(value = "sample.key.no_args.with_default", defaultValue = "DEFAULT_NO_ARGS")
        private String fieldWithKeyNoArgsWithDefault;

        @InjectedResource(value = "sample.key.no_args.with_default", args = {"1", "2"}, defaultValue = "DEFAULT_WITH_ARGS")
        private String fieldWithKeyWithArgsWithDefault;

        @InjectedResource
        private String notFound;

        public String privateField() {
            return privateField;
        }

        public String fieldBySetter() {
            return fieldBySetter;
        }

        public int privateIntField() {
            return privateIntField;
        }

        public int intFieldBySetter() {
            return intFieldBySetter;
        }

        public String fieldWithKey() {
            return fieldWithKey;
        }

        public String fieldWithKeyAndArgs() {
            return fieldWithKeyAndArgs;
        }

        public String fieldWithKeyNoArgsWithDefault() {
            return fieldWithKeyNoArgsWithDefault;
        }

        public String fieldWithKeyWithArgsWithDefault() {
            return fieldWithKeyWithArgsWithDefault;
        }

        public String notFound() {
            return notFound;
        }
    }
}
