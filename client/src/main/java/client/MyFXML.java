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

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import com.google.inject.Injector;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.util.Builder;
import javafx.util.BuilderFactory;
import javafx.util.Callback;
import javafx.util.Pair;

public class MyFXML {

    private Injector injector;

    public MyFXML(Injector injector) {
        this.injector = injector;
    }

    /**
     * Loads and configures a new node with its controller file
     * @param c - The controller class representation of the node of which an instance is to be loaded
     * @param pathToFXML - The relative path to the FXML file of this node
     * @param pathToCSS - The relative path to the CSS file of this node (OPTIONAL: passing null as an argument will apply no CSS)
     * @param <T> The controller of the node that is returned
     * @return A pair containing the controller and the node corresponding to the provided FXML file
     */
    public <T> Pair<T, Parent> load(Class<T> c, String pathToFXML, String pathToCSS) {
        try {
            URL FXMLLocation = MyFXML.class.getClassLoader().getResource(pathToFXML);
            var loader = new FXMLLoader(FXMLLocation, null, null, new MyFactory(), StandardCharsets.UTF_8);
            Parent parent = loader.load();

            if(pathToCSS != null) {
                parent.getStylesheets().add(pathToCSS);
            }

            T ctrl = loader.getController();
            return new Pair<>(ctrl, parent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private class MyFactory implements BuilderFactory, Callback<Class<?>, Object> {

        @Override
        @SuppressWarnings("rawtypes")
        public Builder<?> getBuilder(Class<?> type) {
            return new Builder() {
                @Override
                public Object build() {
                    return injector.getInstance(type);
                }
            };
        }

        @Override
        public Object call(Class<?> type) {
            return injector.getInstance(type);
        }
    }
}