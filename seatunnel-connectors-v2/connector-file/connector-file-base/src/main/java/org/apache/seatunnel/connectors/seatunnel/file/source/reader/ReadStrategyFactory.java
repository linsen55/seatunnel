/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.seatunnel.connectors.seatunnel.file.source.reader;

import org.apache.seatunnel.api.configuration.ReadonlyConfig;
import org.apache.seatunnel.common.exception.CommonErrorCodeDeprecated;
import org.apache.seatunnel.connectors.seatunnel.file.config.BaseSourceConfig;
import org.apache.seatunnel.connectors.seatunnel.file.config.FileFormat;
import org.apache.seatunnel.connectors.seatunnel.file.config.HadoopConf;
import org.apache.seatunnel.connectors.seatunnel.file.exception.FileConnectorException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReadStrategyFactory {

    private ReadStrategyFactory() {}

    public static ReadStrategy of(ReadonlyConfig readonlyConfig, HadoopConf hadoopConf) {
        ReadStrategy readStrategy =
                of(readonlyConfig.get(BaseSourceConfig.FILE_FORMAT_TYPE).name());
        readStrategy.setPluginConfig(readonlyConfig.toConfig());
        readStrategy.init(hadoopConf);
        return readStrategy;
    }

    public static ReadStrategy of(String fileType) {
        try {
            FileFormat fileFormat = FileFormat.valueOf(fileType.toUpperCase());
            return fileFormat.getReadStrategy();
        } catch (IllegalArgumentException e) {
            String errorMsg =
                    String.format(
                            "File source connector not support this file type [%s], please check your config",
                            fileType);
            throw new FileConnectorException(CommonErrorCodeDeprecated.ILLEGAL_ARGUMENT, errorMsg);
        }
    }
}
