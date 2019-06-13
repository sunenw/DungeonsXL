/*
 * Copyright (C) 2012-2019 Frank Baumann
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.erethon.dungeonsxl.event.dplayer.instance.edit;

import de.erethon.dungeonsxl.event.dplayer.DPlayerEvent;
import de.erethon.dungeonsxl.player.DEditPlayer;

/**
 * @author Daniel Saukel
 */
public abstract class DEditPlayerEvent extends DPlayerEvent {

    public DEditPlayerEvent(DEditPlayer dPlayer) {
        super(dPlayer);
    }

    @Override
    public DEditPlayer getDPlayer() {
        return (DEditPlayer) dPlayer;
    }

    public void setDPlayer(DEditPlayer dPlayer) {
        this.dPlayer = dPlayer;
    }

}