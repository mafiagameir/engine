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

import co.mafiagame.common.configuration.CommonConfiguration;
import co.mafiagame.engine.api.GameApi;
import co.mafiagame.engine.container.GameContainer;
import co.mafiagame.engine.domain.GameMood;
import co.mafiagame.engine.executor.CommandExecutor;
import co.mafiagame.test.TestHelper;
import co.mafiagame.test.env.TestInterfaceChannel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author nazila
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = CommonConfiguration.class)
public class Scenario4 {

    @Autowired
    private GameApi gameApi;
    @Autowired
    private CommandExecutor commandExecutor;
    @Autowired
    private GameContainer gameContainer;
    @Autowired
    private TestInterfaceChannel testInterfaceChannel;
    private static final String gameIdentity = "game4";
    private TestHelper helper;

    /**
     * simple game which canceled by users
     *
     * @throws InterruptedException
     */
    @Test
    public void test() throws InterruptedException {
        //start game
        helper = new TestHelper(commandExecutor, gameApi, gameContainer, gameIdentity, 1, 1, true, true);
        commandExecutor.waitUntilOver(helper.ic());
        assertEquals(GameMood.DAY, helper.game().getGameMood());
        assertEquals(4, helper.game().getPlayers().size());
        assertTrue(testInterfaceChannel.containKey("game.started"));

        //one user cancel the game
        gameApi.cancelGame(helper.user(0), helper.username(0));
        commandExecutor.waitUntilOver(helper.ic());
        assertTrue(testInterfaceChannel.lastKeyIs("user.canceled.game"));

        //final election
        gameApi.startFinalElection(helper.user(1));
        commandExecutor.waitUntilOver(helper.ic());
        assertTrue(testInterfaceChannel.lastKeyIs("final.election.started"));

        //two other user cancel game
        gameApi.cancelGame(helper.user(1), helper.username(1));
        gameApi.cancelGame(helper.user(2), helper.username(2));

        commandExecutor.waitUntilOver(helper.ic());
        assertTrue(testInterfaceChannel.lastKeyIs("game.canceled"));

        //game canceled

        gameApi.startFinalElection(helper.ic());
        commandExecutor.waitUntilOver(helper.ic());
        assertTrue(testInterfaceChannel.lastKeyIs("game.not.started.yet"));
    }

}
