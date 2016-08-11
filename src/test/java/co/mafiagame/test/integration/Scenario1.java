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

import co.mafiagame.common.Constants;
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
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author hekmatof
 * @author nazila
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = CommonConfiguration.class)
@Component
public class Scenario1 {
    @Autowired
    private GameApi gameApi;
    @Autowired
    private CommandExecutor commandExecutor;
    @Autowired
    private GameContainer gameContainer;
    @Autowired
    private TestInterfaceChannel testInterfaceChannel;

    private TestHelper helper;

    /**
     * normal game scenario with 10 player which is 6 citizen 2 mafia and one doctor and detective
     *
     * @throws InterruptedException
     */
    @Test
    public void test() throws InterruptedException {
        helper = new TestHelper(commandExecutor, gameApi, gameContainer, "normal-game", 6, 2, true, true);
        assertEquals(GameMood.DAY, helper.game().getGameMood());
        assertEquals(10, helper.game().getPlayers().size());
        assertTrue(testInterfaceChannel.containKey("game.started"));

        //first election
        gameApi.startElection(helper.ic());
        commandExecutor.waitUntilOver(helper.ic());
        assertTrue(testInterfaceChannel.containKey("election.started"));
        userVotes();
        commandExecutor.waitUntilOver(helper.ic());

        //start final election
        gameApi.startFinalElection(helper.ic());
        userVotes();
        //citizen(2) killed
        commandExecutor.waitUntilOver(helper.ic());
        assertTrue(testInterfaceChannel.containKey("user.vote.another"));

        //night mode

        //mafia votes
        gameApi.mafiaKillVote(helper.mafiaIc(0), helper.mafiaUsername(0), helper.citizenUsername(1));
        gameApi.mafiaKillVote(helper.mafiaIc(1), helper.mafiaUsername(1), helper.citizenUsername(1));
        //citizen(1) killed
        commandExecutor.waitUntilOver(helper.ic());
        assertTrue(testInterfaceChannel.containKey("user.vote.another"));

        //DETECTIVE ask
        gameApi.detectiveAsk(helper.detectiveIc(), helper.detectiveUsername(), helper.citizenUsername(5));
        commandExecutor.waitUntilOver(helper.ic());
        assertTrue(testInterfaceChannel.containKey("user.role.is.not.mafia"));

        //doctor heal
        gameApi.doctorHeal(helper.doctorIc(), helper.doctorUsername(), helper.citizenUsername(4));
        commandExecutor.waitUntilOver(helper.ic());
        assertTrue(testInterfaceChannel.containKey("ok"));

        //day mode

        //start final election
        gameApi.startFinalElection(helper.ic());
        commandExecutor.waitUntilOver(helper.ic());
        assertTrue(testInterfaceChannel.containKey("election.started"));
        secondUserVote();
        commandExecutor.waitUntilOver(helper.ic());
        //DETECTIVE killed

        //night mode

        //mafia kills
        gameApi.mafiaKillVote(helper.mafiaIc(0), helper.mafiaUsername(0), Constants.NO_BODY);
        gameApi.mafiaKillVote(helper.mafiaIc(1), helper.mafiaUsername(1), helper.doctorUsername());
        //doctor killed
        commandExecutor.waitUntilOver(helper.ic());
        assertTrue(testInterfaceChannel.containKey("user.vote.another"));

        //doctor heal
        gameApi.doctorHeal(helper.doctorIc(), helper.doctorUsername(), helper.mafiaUsername(1));
        commandExecutor.waitUntilOver(helper.ic());
        assertTrue(testInterfaceChannel.containKey("ok"));

        //day mode

        // a who is playing command
        gameApi.whoIsPlaying(helper.mafiaIc(0));
        commandExecutor.waitUntilOver(helper.ic());
        assertTrue(testInterfaceChannel.lastKeyIs("user.playing"));

        //start final election
        gameApi.startFinalElection(helper.ic());
        thirdUserVote();
        //citizen(0) killed
        commandExecutor.waitUntilOver(helper.ic());
        assertTrue(testInterfaceChannel.containKey("user.vote.another"));

        //night mode

        //mafia kills
        gameApi.mafiaKillVote(helper.mafiaIc(0), helper.mafiaUsername(0), helper.citizenUsername(4));
        gameApi.mafiaKillVote(helper.mafiaIc(1), helper.mafiaUsername(1), helper.citizenUsername(4));
        commandExecutor.waitUntilOver(helper.ic());

        //game over, mafia win
        assertTrue(testInterfaceChannel.containKey("mafia.win"));
        testInterfaceChannel.printMessages();
    }

    private void userVotes() {
        List<String> victims = new ArrayList<>();
        victims.add(helper.mafiaUsername(0));
        victims.add(helper.doctorUsername());
        victims.add(helper.citizenUsername(0));
        gameApi.vote(helper.user(0), helper.username(0), Collections.singletonList(helper.citizenUsername(2)));
        gameApi.vote(helper.user(0), helper.username(0), Collections.singletonList(helper.citizenUsername(2)));
        gameApi.vote(helper.user(1), helper.username(1), victims);
        gameApi.vote(helper.user(2), helper.username(2), Collections.singletonList(helper.citizenUsername(2)));
        gameApi.vote(helper.user(3), helper.username(3), Collections.singletonList(helper.citizenUsername(2)));
        gameApi.vote(helper.user(4), helper.username(4), Collections.singletonList(helper.citizenUsername(2)));
        gameApi.vote(helper.user(5), helper.username(5), Collections.singletonList(helper.citizenUsername(2)));
        gameApi.vote(helper.user(6), helper.username(6), Collections.singletonList(helper.citizenUsername(3)));
        gameApi.vote(helper.user(7), helper.username(7), Collections.singletonList(helper.citizenUsername(2)));
        gameApi.vote(helper.user(8), helper.username(8), Collections.singletonList(helper.doctorUsername()));
        gameApi.vote(helper.user(9), helper.username(9), Collections.singletonList(helper.citizenUsername(2)));
    }


    private void secondUserVote() {
        gameApi.vote(helper.user(0), helper.username(0),
                Collections.singletonList(Constants.NO_BODY));
        gameApi.vote(helper.user(1), helper.username(1), Collections.singletonList(helper.detectiveUsername()));
        gameApi.vote(helper.user(2), helper.username(2), Collections.singletonList(helper.detectiveUsername()));
        gameApi.vote(helper.user(3), helper.username(3), Collections.singletonList(helper.detectiveUsername()));
        gameApi.vote(helper.user(4), helper.username(4), Collections.singletonList(helper.detectiveUsername()));
        gameApi.vote(helper.user(5), helper.username(5), Collections.singletonList(helper.username(6)));
        gameApi.vote(helper.user(6), helper.username(6), Collections.singletonList(helper.detectiveUsername()));
        gameApi.vote(helper.user(7), helper.username(7), Collections.singletonList(helper.username(2)));
    }

    private void thirdUserVote() {
        gameApi.vote(helper.user(0), helper.username(0), Collections.singletonList(helper.citizenUsername(0)));
        gameApi.vote(helper.user(1), helper.username(1), Collections.singletonList(helper.citizenUsername(0)));
        gameApi.vote(helper.user(2), helper.username(2), Collections.singletonList(helper.citizenUsername(0)));
        gameApi.vote(helper.user(3), helper.username(3), Collections.singletonList(helper.citizenUsername(0)));
        gameApi.vote(helper.user(4), helper.username(4), Collections.singletonList(helper.mafiaUsername(0)));
        gameApi.vote(helper.user(5), helper.username(5), Collections.singletonList(helper.citizenUsername(0)));
    }

}
