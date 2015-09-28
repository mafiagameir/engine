/*
 * Copyright (C) 2015 mafiagame.ir
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package co.mafiagame.engine.executor;

import co.mafiagame.common.channel.InterfaceChannel;
import co.mafiagame.common.channel.InterfaceContext;
import co.mafiagame.common.domain.result.ChannelType;
import co.mafiagame.common.domain.result.Message;
import co.mafiagame.common.domain.result.ResultMessage;
import co.mafiagame.common.exception.MafiaException;
import co.mafiagame.engine.command.Command;
import co.mafiagame.engine.command.context.CommandContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author hekmatof
 */
@Component
public class CommandExecutor {
    private final static Logger logger = LoggerFactory
            .getLogger(CommandExecutor.class);
    private Map<String, Command> commandsMap;
    //private final Map<InterfaceContext, ThreadPoolExecutor> executors = new HashMap<>();
    private ThreadPoolExecutor singleThread;
    @Autowired
    private InterfaceChannel channel;
    @Autowired
    private List<Command> commands;
    @Value("${mafia.thread.keep.alive}")
    private int keepAlive;

    @PostConstruct
    public void init() {
        commandsMap = new HashMap<>();
        commands.stream().forEach(c -> commandsMap.put(c.commandName(), c));
        singleThread = new ThreadPoolExecutor(0, 1, keepAlive, TimeUnit.MINUTES, new LinkedBlockingQueue<>());
    }

    private ThreadPoolExecutor getExecutor(InterfaceContext interfaceContext) {
        return singleThread;
        /*if (executors.containsKey(interfaceContext))
            return executors.get(interfaceContext);
        ThreadPoolExecutor executorService = new ThreadPoolExecutor(0, 1, keepAlive, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>());
        executors.put(interfaceContext, executorService);

        return executorService;*/

    }

    public void waitUntilOver(InterfaceContext ic) throws InterruptedException {
        while (getExecutor(ic).getQueue().size() != 0 || getExecutor(ic).getActiveCount() != 0)
            Thread.sleep(50);
    }

    @SuppressWarnings("unchecked")
    public void run(InterfaceContext interfaceContext, String commandKey,
                    CommandContext context) {
        getExecutor(interfaceContext)
                .submit(() -> {
                    try {
                        ResultMessage resultMessage = commandsMap.get(commandKey).execute(context);
                        if (resultMessage.getChannelType() != ChannelType.NONE) {
                            logger.info("on game {} : {}", interfaceContext, resultMessage);
                            channel.send(resultMessage);
                        }
                    } catch (MafiaException e) {
                        ResultMessage resultMessage = new ResultMessage(
                                new Message(e.getMessageCode(), interfaceContext.getUserId(),
                                        interfaceContext.getUserName(), e.getMessageArgs()),
                                interfaceContext.getSenderType(), interfaceContext);
                        logger.warn("on game {} :{}", interfaceContext, resultMessage);
                        channel.send(resultMessage);
                    } catch (Exception e) {
                        logger.error("on game {} system error occurred", interfaceContext, e);
                    }
                });
    }
}
