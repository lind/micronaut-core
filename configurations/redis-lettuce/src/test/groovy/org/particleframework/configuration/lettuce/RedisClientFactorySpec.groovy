/*
 * Copyright 2017 original authors
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */
package org.particleframework.configuration.lettuce

import io.lettuce.core.RedisClient
import org.particleframework.context.ApplicationContext
import org.particleframework.core.io.socket.SocketUtils
import redis.embedded.RedisServer
import spock.lang.Specification

/**
 * @author Graeme Rocher
 * @since 1.0
 */
class RedisClientFactorySpec extends Specification{

    void "test redis server config by port"() {
        given:
        def port = SocketUtils.findAvailableTcpPort()
        RedisServer redisServer = new RedisServer(port)
        redisServer.start()

        when:
        ApplicationContext applicationContext = ApplicationContext.run('particle.redis.port':port)
        RedisClient client = applicationContext.getBean(RedisClient)

        def command = client.connect().sync()
        then:
        command.set("foo", "bar")
        command.get("foo") == "bar"

        cleanup:redisServer.stop()
    }

    void "test redis server config by URI"() {
        given:
        def port = SocketUtils.findAvailableTcpPort()
        RedisServer redisServer = new RedisServer(port)
        redisServer.start()

        when:
        ApplicationContext applicationContext = ApplicationContext.run('particle.redis.uri':"redis://localhost:$port")
        RedisClient client = applicationContext.getBean(RedisClient)
        def command = client.connect().sync()
        then:
        command.set("foo", "bar")
        command.get("foo") == "bar"

        cleanup:redisServer.stop()
    }
}