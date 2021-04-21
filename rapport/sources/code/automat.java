while(! automat.isFinished())
            {
                if(automat.isValidState())
                {
                    State currentState = automat.getState();

                    switch (currentState) {

                        case LIGHT_SOURCE: {
                            automat.setState(new EtatLightSource());
                            PositionnalLight light = (PositionnalLight) automat.action();
                            scene.addLight(light);
                            break;
                        }

                        case SPHERE: {
                            automat.setState(new EtatSphere());
                            Shape sphere = (Shape) automat.action();
                            scene.addShape(sphere);
                            break;
                        }

                        case OUTSIDE: {
                            automat.setState(new EtatOutside());
                            automat.action();
                            break;
                        }

                    ...