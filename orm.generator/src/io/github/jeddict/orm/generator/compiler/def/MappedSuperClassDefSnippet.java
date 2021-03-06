/**
 * Copyright 2013-2018 the original author or authors from the Jeddict project (https://jeddict.github.io/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package io.github.jeddict.orm.generator.compiler.def;

import static io.github.jeddict.jcode.jpa.JPAConstants.MAPPED_SUPERCLASS;
import static io.github.jeddict.jcode.jpa.JPAConstants.MAPPED_SUPERCLASS_FQN;
import io.github.jeddict.orm.generator.compiler.InvalidDataException;
import io.github.jeddict.orm.generator.util.ImportSet;

public class MappedSuperClassDefSnippet extends IdentifiableClassDefSnippet {

    @Override
    public String getManagedType() {
        return "@" + MAPPED_SUPERCLASS;
    }

    @Override
    public ImportSet getImportSet() throws InvalidDataException {
        ImportSet importSnippets = super.getImportSet();

        importSnippets.add(MAPPED_SUPERCLASS_FQN);

        return importSnippets;
    }
}
