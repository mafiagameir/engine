/*
 *  Copyright (C) 2015 mafiagame.ir
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
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
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Esa Hekmatizadeh
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CommonConfiguration.class)
@Component
public class Scenario5 {

    @Autowired
    private GameApi gameApi;
    @Autowired
    private CommandExecutor commandExecutor;
    @Autowired
    private GameContainer gameContainer;
    @Autowired
    private TestInterfaceChannel testInterfaceChannel;

    private TestHelper helper;

    @Test
    public void test() throws InterruptedException {
        helper = new TestHelper(commandExecutor, gameApi, gameContainer, "killMe-game", 3, 1, false, false);
        assertEquals(GameMood.DAY, helper.game().getGameMood());
        assertEquals(4, helper.game().getPlayers().size());
        assertTrue(testInterfaceChannel.containKey("game.started"));

        gameApi.killMe(helper.citizenIc(0));
        commandExecutor.waitUntilOver(helper.ic());
        assertTrue(testInterfaceChannel.containKey("user.exit.game"));

        gameApi.killMe(helper.citizenIc(1));
        commandExecutor.waitUntilOver(helper.ic());
        assertTrue(testInterfaceChannel.containKey("user.exit.game"));
        assertTrue(testInterfaceChannel.containKey("mafia.win"));

    }
}
