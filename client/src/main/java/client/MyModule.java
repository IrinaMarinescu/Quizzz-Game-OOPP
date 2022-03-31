/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package client;

import client.scenes.AdminInterfaceCtrl;
import client.scenes.LeaderboardCtrl;
import client.scenes.MainCtrl;
import client.scenes.MainFrameCtrl;
import client.scenes.QuestionFrameCtrl;
import client.scenes.framecomponents.EmoteCtrl;
import client.scenes.framecomponents.TimerBarCtrl;
import client.scenes.questioncontrollers.InsteadOfQuestionCtrl;
import client.scenes.questioncontrollers.OpenQuestionCtrl;
import client.scenes.questioncontrollers.QuestionOneImageCtrl;
import client.scenes.questioncontrollers.QuestionThreePicturesCtrl;
import client.scenes.questioncontrollers.QuestionTrueFalseCtrl;
import client.utils.LongPollingUtils;
import client.utils.ServerUtils;
import client.utils.TimeUtils;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Scopes;

/**
 * The module used for dependency injection
 */
public class MyModule implements Module {

    @Override
    public void configure(Binder binder) {
        binder.bind(MainCtrl.class).in(Scopes.SINGLETON);
        binder.bind(MainFrameCtrl.class).in(Scopes.SINGLETON);
        binder.bind(LeaderboardCtrl.class).in(Scopes.SINGLETON);
        binder.bind(AdminInterfaceCtrl.class).in(Scopes.SINGLETON);
        binder.bind(QuestionFrameCtrl.class).in(Scopes.SINGLETON);
        binder.bind(TimerBarCtrl.class).in(Scopes.SINGLETON);
        binder.bind(EmoteCtrl.class).in(Scopes.SINGLETON);
        binder.bind(OpenQuestionCtrl.class).in(Scopes.SINGLETON);
        binder.bind(InsteadOfQuestionCtrl.class).in(Scopes.SINGLETON);
        binder.bind(QuestionOneImageCtrl.class).in(Scopes.SINGLETON);
        binder.bind(QuestionThreePicturesCtrl.class).in(Scopes.SINGLETON);
        binder.bind(QuestionTrueFalseCtrl.class).in(Scopes.SINGLETON);
        /**
         * bind utilies below
         * **/
        binder.bind(ServerUtils.class).in(Scopes.SINGLETON);
        binder.bind(TimeUtils.class).in(Scopes.SINGLETON);
        binder.bind(LongPollingUtils.class).in(Scopes.SINGLETON);

    }
}
