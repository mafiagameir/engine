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

package co.mafiagame.engine.util;

import co.mafiagame.common.Constants;
import co.mafiagame.engine.command.context.EmptyContext;
import co.mafiagame.engine.container.GameContainer;
import co.mafiagame.engine.executor.CommandExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Esa Hekmatizadeh
 */
@Component
public class PurgeTimer extends TimerTask {
    @Autowired
    private GameContainer gameContainer;
    @Autowired
    private CommandExecutor commandExecutor;

    @PostConstruct
    public void initPurgeTimer() {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.SECOND,10);
        //now.add(Calendar.DATE, 1);
        //now.set(Calendar.HOUR_OF_DAY, 6);
        Timer timer = new Timer();
        timer.schedule(this, now.getTime(), 5000);
    }

    @Override
    public void run() {
        Calendar twoDayAgo = Calendar.getInstance();
        twoDayAgo.add(Calendar.DATE, -2);
        Calendar oneDayAgo = Calendar.getInstance();
        oneDayAgo.add(Calendar.DATE, -1);
        gameContainer.getGames().forEach(game -> {
            Calendar gameLastUpdate= Calendar.getInstance();
            gameLastUpdate.setTime(game.getLastUpdate());
            if (twoDayAgo.after(gameLastUpdate))
                commandExecutor.run(game.getInterfaceContext(),
                        Constants.CMD.Internal.PURGE,
                        new EmptyContext(game.getInterfaceContext(), game));
            else if(oneDayAgo.after(gameLastUpdate))
                commandExecutor.run(game.getInterfaceContext(),
                        Constants.CMD.Internal.PURGE_ALARM,
                        new EmptyContext(game.getInterfaceContext(), game));
        });
    }
}
