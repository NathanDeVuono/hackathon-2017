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

import com.battlesnake.data.*;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@RestController
public class RequestController {

  @RequestMapping(value="/start", method=RequestMethod.POST, produces="application/json")
  public StartResponse start(@RequestBody StartRequest request) {
    return new StartResponse()
      .setName("Climbing Snake Man")
      .setColor("#FF0000")
      .setHeadUrl("http://vignette1.wikia.nocookie.net/nintendo/images/6/61/Bowser_Icon.png/revision/latest?cb=20120820000805&path-prefix=en")
      .setHeadType(HeadType.DEAD)
      .setTailType(TailType.PIXEL)
      .setTaunt("Roarrrrrrrrr!");
  }

  @RequestMapping(value="/move", method=RequestMethod.POST, produces = "application/json")
  public MoveResponse move(@RequestBody MoveRequest request) {
    char[][] map = makeMap(request);
    Snake s = getMySnake(request);

    Move m = null;
    switch (request.getTurn() % 4) {
    case 0:
      m = Move.DOWN;
      break;
    case 1:
      m = Move.RIGHT;
      break;
    case 2:
      m = Move.UP;
      break;
    case 3:
      m = Move.LEFT;
      break;
    }
    return new MoveResponse()
            .setMove(m);
  }
    
  @RequestMapping(value="/end", method=RequestMethod.POST)
  public Object end() {
      // No response required
      Map<String, Object> responseObject = new HashMap<String, Object>();
      return responseObject;
  }

  private Snake getMySnake(MoveRequest request) {
    String me = request.getYou();
    for (Snake s : request.getSnakes()) {
      if (s.getId() == me) {
        return s;
      }
    }
    return null;
  }

  char[][] makeMap(MoveRequest request) {
    char[][] map = new char[request.getWidth()][request.getHeight()];
    for (int[] f : request.getFood()) {
      map[f[0]][f[1]] = 'f'; // food
    }
    for (Snake s : request.getSnakes()) {
      for (int[] b : s.getCoords()) {
        if (s.getId() == request.getYou()) {
          map[b[0]][b[1]] = 's'; // self head
        } else {
          map[b[0]][b[1]] = 'o'; // opponent head
        }
        map[b[0]][b[1]] = 'x'; // obstacle
      }
    }
    return map;
  }

}
