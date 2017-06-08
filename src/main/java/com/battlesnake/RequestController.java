/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.battlesnake;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.battlesnake.data.HeadType;
import com.battlesnake.data.Move;
import com.battlesnake.data.MoveRequest;
import com.battlesnake.data.MoveResponse;
import com.battlesnake.data.Snake;
import com.battlesnake.data.StartRequest;
import com.battlesnake.data.StartResponse;
import com.battlesnake.data.TailType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RequestController {

    private int height;
    private int width;

    @RequestMapping(value="/start", method=RequestMethod.POST, produces="application/json")
    public StartResponse start(@RequestBody StartRequest request) {
        return new StartResponse()
                .setName("Bowser Snake")
                .setColor("#FF0000")
                .setHeadUrl("http://vignette1.wikia.nocookie.net/nintendo/images/6/61/Bowser_Icon.png/revision/latest?cb=20120820000805&path-prefix=en")
                .setHeadType(HeadType.DEAD)
                .setTailType(TailType.PIXEL)
                .setTaunt("Roarrrrrrrrr!");
    }

    @RequestMapping(value="/move", method=RequestMethod.POST, produces = "application/json")
    public MoveResponse move(@RequestBody MoveRequest request) {
        int[][] food = request.getFood();

        Snake me = request.getSnakes().stream().filter(s -> s.getId().equals(request.getYou())).findAny().orElse(null);
        int[][] myPos = me.getCoords();

        System.out.println("My post head:" + myPos[0][0]);
        for (int x = 0; x < myPos.length; x++) {
            for (int y = 0; y < myPos[x].length; y++) {
                System.out.println("My body:" + myPos[x][y]);
            }
        }

        List<Snake> activeSnakes = request.getSnakes().stream()
                .filter(s -> !request.getDeadSnakes().contains(s) &&
                        s.getName().equals(request.getYou())
                ).collect(Collectors.toList());

        if (myPos[0][1] < request.getHeight()) {
            return new MoveResponse()
                    .setMove(Move.DOWN)
                    .setTaunt("Going Down!");
        } else if (myPos[0][0] > 0) {
            return new MoveResponse()
                    .setMove(Move.LEFT)
                    .setTaunt("Going left!");
        } else if (myPos[0][1] > 0) {
            return new MoveResponse()
                    .setMove(Move.UP)
                    .setTaunt("Going up!");
        } else {
            return new MoveResponse()
                    .setMove(Move.RIGHT)
                    .setTaunt("Going right!");
        }
    }

    @RequestMapping(value="/end", method=RequestMethod.POST)
    public Object end() {
        // No response required
        Map<String, Object> responseObject = new HashMap<String, Object>();
        return responseObject;
    }
}