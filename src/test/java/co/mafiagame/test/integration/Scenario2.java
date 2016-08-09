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
 * @author hekmatof
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CommonConfiguration.class)
public class Scenario2 {
    @Autowired
    private GameApi gameApi;
    @Autowired
    private CommandExecutor commandExecutor;
    @Autowired
    private GameContainer gameContainer;
    @Autowired
    private TestInterfaceChannel testInterfaceChannel;
    private InterfaceContext starter = new TestInterfaceContext(gameIdentity, "starter", ChannelType.GENERAL);
    private static final String gameIdentity = "game2";
    private TestHelper helper;

    /**
     * a game with lots of exceptions , which check unusual situations
     *
     * @throws InterruptedException
     */
    @Test
    public void test() throws InterruptedException {
        startGameHalf();
        commandExecutor.waitUntilOver(starter);
        assertEquals(null, gameContainer.getGame(starter));
        commandExecutor.waitUntilOver(starter);
        firstElection();
        commandExecutor.waitUntilOver(starter);
        assertTrue(testInterfaceChannel.lastKeyIs("game.not.started.yet"));
        startGameFinal();
        commandExecutor.waitUntilOver(starter);
        helper = new TestHelper(gameContainer, gameIdentity, gameContainer.getGame(starter));
        assertEquals(GameMood.DAY, helper.game().getGameMood());
        assertEquals(10, helper.game().getPlayers().size());
        assertTrue(testInterfaceChannel.containKey("game.started"));

        gameApi.startFinalElection(helper.ic());
        commandExecutor.waitUntilOver(helper.ic());
        assertTrue(testInterfaceChannel.containKey("final.election.started"));
        userVotes(); //votes are equal
        commandExecutor.waitUntilOver(helper.ic());
        assertTrue(testInterfaceChannel.containKey("user.vote.another"));

        // a citizen try kill command
        citizenKill();
        commandExecutor.waitUntilOver(helper.ic());
        assertTrue(testInterfaceChannel.lastKeyIs("not.time.of.mafia.vote"));

        //mafia try kill command in day
        mafiaKillOnDay();
        commandExecutor.waitUntilOver(helper.ic());
        assertTrue(testInterfaceChannel.lastKeyIs("not.time.of.mafia.vote"));

        //election without start command
        userVotes();
        commandExecutor.waitUntilOver(helper.ic());
        assertTrue(testInterfaceChannel.lastKeyIs("no.election.started"));

        //DETECTIVE ask in day
        detectiveAsk();
        commandExecutor.waitUntilOver(helper.ic());
        assertTrue(testInterfaceChannel.lastKeyIs("you.cant.ask.now"));

        //doctor heal on day
        doctorHeal();
        commandExecutor.waitUntilOver(helper.ic());
        assertTrue(testInterfaceChannel.lastKeyIs("not.time.of.doctor.heal"));

        gameApi.startFinalElection(helper.ic());
        gameApi.startFinalElection(helper.ic());
        commandExecutor.waitUntilOver(helper.ic());
        assertTrue(testInterfaceChannel.containKey("final.election.started"));
        userVotesNobodyAndPlayerNotExist();
        commandExecutor.waitUntilOver(helper.ic());
        assertTrue(testInterfaceChannel.containKey("player.not.found"));

        userVotesCorrect();
        commandExecutor.waitUntilOver(helper.ic());
        assertTrue(testInterfaceChannel.containKey("player.was.killed.with.maximum.votes"));
        //citizen(0) killed

        //night mode

        //testing vote on night
        gameApi.startFinalElection(helper.ic());
        commandExecutor.waitUntilOver(helper.ic());
        assertTrue(testInterfaceChannel.containKey("vote.on.night.not.allowed"));
        userVoteOnNight();
        commandExecutor.waitUntilOver(helper.ic());
        assertTrue(testInterfaceChannel.lastKeyIs("vote.on.night.not.allowed"));

        //citizen try kill command
        citizenKillOnNight();
        commandExecutor.waitUntilOver(helper.ic());
        assertTrue(testInterfaceChannel.lastKeyIs("you.are.not.mafia"));

        //DETECTIVE ask in mafia turn
        detectiveAsk();
        commandExecutor.waitUntilOver(helper.ic());
        assertTrue(testInterfaceChannel.lastKeyIs("you.cant.ask.now"));

        //doctor heal in mafia turn
        doctorHeal();
        commandExecutor.waitUntilOver(helper.ic());
        assertTrue(testInterfaceChannel.lastKeyIs("not.time.of.doctor.heal"));

        //mafia kill command
        mafiaKillVote();
        commandExecutor.waitUntilOver(helper.ic());
        assertTrue(testInterfaceChannel.containKey("user.vote.another"));

        //DETECTIVE ask in general
        detectiveAskInGeneral();
        commandExecutor.waitUntilOver(helper.ic());
        assertTrue(testInterfaceChannel
                .lastKeyIs("command.is.unavailable.here"));

        //citizen ask instead of DETECTIVE
        citizenAskInsteaddetective();
        commandExecutor.waitUntilOver(helper.ic());
        assertTrue(testInterfaceChannel.containKey("you.are.not.detective"));

        //DETECTIVE ask
        detectiveAsk();
        commandExecutor.waitUntilOver(helper.ic());
        assertTrue(testInterfaceChannel.containKey("user.role.is.mafia"));

        //citizen heal instead of doctor
        citizenHealInsteadDoctor();
        commandExecutor.waitUntilOver(helper.ic());
        assertTrue(testInterfaceChannel.lastKeyIs("you.are.not.doctor"));

        //doctor heal in general room
        doctorHealInGeneral();
        commandExecutor.waitUntilOver(helper.ic());
        assertTrue(testInterfaceChannel
                .lastKeyIs("you.are.not.in.doctor.private"));

        //doctor heal
        doctorHeal();
        commandExecutor.waitUntilOver(helper.ic());
        assertTrue(testInterfaceChannel
                .containKey("ok"));
        testInterfaceChannel.printMessages();
    }

    private void startGameHalf() throws InterruptedException {
        gameApi.startStashedGame(starter, 5, 3, 1, 1);
        for (int i = 0; i < 9; i++) {
            TestInterfaceContext userIc = new TestInterfaceContext(
                    gameIdentity, "user" + i, ChannelType.GENERAL);
            gameApi.register(userIc, null, null);
        }
    }

    private void startGameFinal() throws InterruptedException {
        TestInterfaceContext userIc = new TestInterfaceContext(
                gameIdentity, "user9", ChannelType.GENERAL);
        gameApi.register(userIc, null, null);
    }

    public void firstElection() throws InterruptedException {
        TestInterfaceContext ic = new TestInterfaceContext(gameIdentity, "esa",
                ChannelType.GENERAL);
        gameApi.startElection(ic);
    }

    private void userVotes() {
        List<String> victims = new ArrayList<>();
        victims.add(helper.mafiaUsername(0));
        victims.add(helper.doctorUsername());
        victims.add(helper.citizenUsername(0));
        gameApi.vote(helper.user(0), helper.username(0), Collections.singletonList(helper.citizenUsername(2)));
        gameApi.vote(helper.user(1), helper.username(1), Collections.singletonList(helper.citizenUsername(2)));
        gameApi.vote(helper.user(2), helper.username(2), Collections.singletonList(helper.citizenUsername(0)));
        gameApi.vote(helper.user(3), helper.username(3), Collections.singletonList(helper.citizenUsername(2)));
        gameApi.vote(helper.user(4), helper.username(4), Collections.singletonList(helper.citizenUsername(2)));
        gameApi.vote(helper.user(5), helper.username(5), Collections.singletonList(helper.citizenUsername(2)));
        gameApi.vote(helper.user(6), helper.username(6), victims);
        gameApi.vote(helper.user(7), helper.username(7), Collections.singletonList(helper.citizenUsername(0)));
        gameApi.vote(helper.user(8), helper.username(8), Collections.singletonList(helper.citizenUsername(0)));
        gameApi.vote(helper.user(9), helper.username(9), Collections.singletonList(helper.citizenUsername(0)));
    }

    private void userVotesNobodyAndPlayerNotExist() {
        List<String> victims = new ArrayList<>();
        victims.add(helper.mafiaUsername(0));
        victims.add(helper.doctorUsername());
        victims.add(helper.citizenUsername(0));
        gameApi.vote(helper.user(0), helper.username(0), Collections.singletonList(helper.citizenUsername(0)));
        gameApi.vote(helper.user(1), helper.username(1), Collections.singletonList(helper.citizenUsername(0)));
        gameApi.vote(helper.user(2), helper.username(2), Collections.singletonList(helper.citizenUsername(0)));
        gameApi.vote(helper.user(3), helper.username(3), Collections.singletonList(helper.citizenUsername(2)));
        gameApi.vote(helper.user(4), helper.username(4), Collections.singletonList(helper.citizenUsername(0)));
        gameApi.vote(helper.user(5), helper.username(5), Collections.singletonList(helper.citizenUsername(0)));
        gameApi.vote(helper.user(6), helper.username(6), victims);
        gameApi.vote(helper.user(7), helper.username(7), Collections.singletonList(helper.citizenUsername(0)));
        gameApi.vote(helper.user(8), helper.username(8), Collections.singletonList(Constants.NO_BODY));
        gameApi.vote(helper.user(9), helper.username(9), Collections.singletonList("soghra"));
    }

    private void userVotesCorrect() {
        List<String> victims = new ArrayList<>();
        victims.add(helper.mafiaUsername(0));
        victims.add(helper.doctorUsername());
        victims.add(helper.citizenUsername(0));
        gameApi.vote(helper.user(0), helper.username(0), Collections.singletonList(helper.citizenUsername(0)));
        gameApi.vote(helper.user(1), helper.username(1), Collections.singletonList(helper.citizenUsername(0)));
        gameApi.vote(helper.user(2), helper.username(2), Collections.singletonList(helper.citizenUsername(0)));
        gameApi.vote(helper.user(3), helper.username(3), Collections.singletonList(helper.citizenUsername(2)));
        gameApi.vote(helper.user(4), helper.username(4), Collections.singletonList(helper.citizenUsername(0)));
        gameApi.vote(helper.user(5), helper.username(5), Collections.singletonList(helper.citizenUsername(0)));
        gameApi.vote(helper.user(6), helper.username(6), victims);
        gameApi.vote(helper.user(7), helper.username(7), Collections.singletonList(helper.citizenUsername(0)));
        gameApi.vote(helper.user(8), helper.username(8), Collections.singletonList(helper.citizenUsername(0)));
        gameApi.vote(helper.user(9), helper.username(9), Collections.singletonList(Constants.NO_BODY));
    }

    private void userVoteOnNight() {
        List<String> victims = new ArrayList<>();
        victims.add(helper.mafiaUsername(0));
        victims.add(helper.doctorUsername());
        victims.add(helper.citizenUsername(0));
        gameApi.vote(helper.user(0), helper.username(0), Collections.singletonList(helper.citizenUsername(0)));
        gameApi.vote(helper.user(1), helper.username(1), Collections.singletonList(helper.citizenUsername(0)));
        gameApi.vote(helper.user(2), helper.username(2), Collections.singletonList(helper.citizenUsername(0)));
        gameApi.vote(helper.user(3), helper.username(3), Collections.singletonList(helper.citizenUsername(2)));
        gameApi.vote(helper.user(4), helper.username(4), Collections.singletonList(helper.citizenUsername(0)));
        gameApi.vote(helper.user(5), helper.username(5), Collections.singletonList(helper.citizenUsername(0)));
        gameApi.vote(helper.user(6), helper.username(6), victims);
        gameApi.vote(helper.user(7), helper.username(7), Collections.singletonList(helper.citizenUsername(0)));
    }

    private void citizenKill() {
        gameApi.mafiaKillVote(helper.citizenIc(1, ChannelType.GENERAL), helper.citizenUsername(1), helper.mafiaUsername(1));
    }

    private void mafiaKillOnDay() {
        gameApi.mafiaKillVote(helper.mafiaIc(0), helper.mafiaUsername(0), helper.citizenUsername(0));
    }

    private void citizenKillOnNight() {
        gameApi.mafiaKillVote(helper.citizenIc(1), helper.citizenUsername(1), helper.mafiaUsername(0));
    }

    private void detectiveAsk() {
        gameApi.detectiveAsk(helper.detectiveIc(), helper.detectiveUsername(), helper.mafiaUsername(0));
    }

    private void mafiaKillVote() {
        gameApi.mafiaKillVote(helper.mafiaIc(0), helper.mafiaUsername(0), helper.citizenUsername(1));
        gameApi.mafiaKillVote(helper.mafiaIc(1), helper.mafiaUsername(1), helper.citizenUsername(1));
        gameApi.mafiaKillVote(helper.mafiaIc(2), helper.mafiaUsername(2), helper.citizenUsername(2));
    }

    private void detectiveAskInGeneral() {
        gameApi.detectiveAsk(helper.detectiveIc(ChannelType.GENERAL), helper.detectiveUsername(),
                helper.mafiaUsername(0));
    }

    private void citizenAskInsteaddetective() {
        gameApi.detectiveAsk(helper.citizenIc(3), helper.citizenUsername(3), helper.mafiaUsername(0));
    }

    private void citizenHealInsteadDoctor() {
        gameApi.doctorHeal(helper.citizenIc(3), helper.citizenUsername(3), helper.citizenUsername(1));
    }

    private void doctorHealInGeneral() {
        gameApi.doctorHeal(helper.doctorIc(ChannelType.GENERAL), helper.doctorUsername(), helper.citizenUsername(1));
    }

    private void doctorHeal() {
        gameApi.doctorHeal(helper.doctorIc(), helper.doctorUsername(), helper.citizenUsername(3));
    }
}
