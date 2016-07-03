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

package co.mafiagame.engine.container;

import co.mafiagame.common.channel.InterfaceContext;
import co.mafiagame.engine.domain.Game;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author hekmatof
 */
@Component
public class GameContainer extends Container<Game> {
    private final Map<InterfaceContext, Game> games = new HashMap<>();
    public static final String DIR = "games";

    @Value("${mafia.game.persistence.location}")
    private String gameLocation;

    @PostConstruct
    private void init() throws IOException {
        load();
        createPersistTimer();
    }

    public void addGame(Game game) throws InterruptedException {
        games.put(game.getInterfaceContext(), game);
    }

    public Game getGame(InterfaceContext interfaceContext) {
        return games.get(interfaceContext);
    }

    public void finished(InterfaceContext ic) throws IOException {
        games.remove(ic);
        removeFile(ic);
    }

    public Collection<Game> getGames() {
        return games.values();
    }

    @Override
    protected String getGameLocation() {
        return gameLocation;
    }

    @Override
    protected String getDir() {
        return DIR;
    }

    @Override
    protected Map<InterfaceContext, Game> getMap() {
        return games;
    }

    @Override
    protected Class<Game> getClazz() {
        return Game.class;
    }
}
