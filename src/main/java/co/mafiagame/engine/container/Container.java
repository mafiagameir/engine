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

package co.mafiagame.engine.container;

import co.mafiagame.common.channel.InterfaceContext;
import co.mafiagame.engine.domain.InterfaceContextAware;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.JavaSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * @author Esa Hekmatizadeh
 */
public abstract class Container<T extends InterfaceContextAware> {
    private final static Logger logger = LoggerFactory.getLogger(Container.class);
    private final Kryo kryo = new Kryo();

    protected abstract String getGameLocation();

    protected abstract String getDir();

    protected abstract Map<InterfaceContext, T> getMap();

    protected abstract Class<T> getClazz();

    public Container() {
        kryo.register(UUID.class, new JavaSerializer());
    }

    protected File directory() throws IOException {
        File gameDirectory = new File(getGameLocation() + File.separator + getDir());
        if (!gameDirectory.exists() && !gameDirectory.mkdir())
            throw new IOException("could now create game directory");
        return gameDirectory;
    }

    protected void removeFile(InterfaceContext ic) throws IOException {
        new File(getGameLocation(), ic.getRoomId()).delete();
    }

    protected void createPersistTimer() {
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                getMap().values().forEach(g -> {
                    Output output = null;
                    try {
                        logger.debug("persisting objects into {}", getGameLocation() + File.separator + getDir());
                        File gameFile = new File(directory(), g.getInterfaceContext().getRoomId());
                        output = new Output(new FileOutputStream(gameFile));
                        kryo.writeObject(output, g);
                    } catch (IOException e) {
                        logger.error("could not save object file: {}", g, e);
                    } finally {
                        if (!Objects.isNull(output))
                            output.close();
                    }
                });
            }
        };
        timer.schedule(timerTask, new Date(), TimeUnit.MINUTES.toMillis(5));
    }

    protected void load() throws IOException {
        File[] gameFiles = directory().listFiles();
        if (Objects.isNull(gameFiles))
            throw new IOException("could not read game directory");
        Stream.of(gameFiles).map(f -> {
            try {
                Input input = new Input(new FileInputStream(f));
                T g = kryo.readObject(input, getClazz());
                input.close();
                return g;
            } catch (IOException e) {
                logger.error("could not read file: {}", f.getPath() + File.separator + f.getName(), e);
                System.exit(1);
                return null;
            }
        }).forEach(g -> {
            logger.info("loading object from directory: {}", g);
            getMap().put(g.getInterfaceContext(), g);
        });
    }
}
