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

package co.mafiagame.test.integration;

import co.mafiagame.common.channel.InterfaceContext;
import co.mafiagame.common.configuration.CommonConfiguration;
import co.mafiagame.common.domain.result.ChannelType;
import co.mafiagame.engine.api.GameApi;
import co.mafiagame.engine.container.GameContainer;
import co.mafiagame.engine.domain.GameMood;
import co.mafiagame.engine.executor.CommandExecutor;
import co.mafiagame.test.TestHelper;
import co.mafiagame.test.env.TestInterfaceChannel;
import co.mafiagame.test.env.TestInterfaceContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author nazila
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CommonConfiguration.class)
public class Scenario3 {

    @Autowired
    private GameApi gameApi;
    @Autowired
    private CommandExecutor commandExecutor;
    @Autowired
    private GameContainer gameContainer;
    @Autowired
    private TestInterfaceChannel testInterfaceChannel;
    private static final String gameIdentity = "game3";
    private TestHelper helper;

    /**
     * very simple and normal game with one mafia which is detected by users in first round
     *
     * @throws InterruptedException
     */
    @Test
    public void test() throws InterruptedException {
        //check help command
        InterfaceContext somebody = new TestInterfaceContext(gameIdentity, "somebody", ChannelType.USER_PRIVATE);
        gameApi.help(somebody);
        commandExecutor.waitUntilOver(somebody);
        assertTrue(testInterfaceChannel.containKey("help"));

        //start game
        helper = new TestHelper(commandExecutor, gameApi, gameContainer, gameIdentity, 1, 1, true, true);
        commandExecutor.waitUntilOver(helper.ic());
        assertEquals(GameMood.DAY, helper.game().getGameMood());
        assertEquals(4, helper.game().getPlayers().size());
        assertTrue(testInterfaceChannel.containKey("game.started"));

        //final election
        gameApi.startFinalElection(helper.user(0));
        commandExecutor.waitUntilOver(helper.ic());
        assertTrue(testInterfaceChannel.containKey("final.election.started"));

        //user vote for mafia member
        userVotes();
        commandExecutor.waitUntilOver(helper.ic());
        assertTrue(testInterfaceChannel.containKey("citizens.win"));
        testInterfaceChannel.printMessages();
    }

    private void userVotes() {
        List<String> victims = new ArrayList<>();
        victims.add(helper.mafiaUsername(0));
        victims.add(helper.doctorUsername());
        victims.add(helper.citizenUsername(0));
        gameApi.vote(helper.user(0), helper.username(0), Collections.singletonList(helper.mafiaUsername(0)));
        gameApi.vote(helper.user(1), helper.username(1), Collections.singletonList(helper.mafiaUsername(0)));
        gameApi.vote(helper.user(2), helper.username(2), victims);
        gameApi.vote(helper.user(3), helper.username(3), Collections.singletonList(helper.mafiaUsername(0)));
    }
}
