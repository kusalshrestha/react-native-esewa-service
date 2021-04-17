import { NativeModules } from 'react-native';

enum Environment {
  PRODUCTION = "live",
  TEST = "test",
  LOCAL = "local"
}

type EsewaServiceType = {
  init(clientId: string, secret: string, environment: Environment): void;
};

const { Esewa } = NativeModules;

export default Esewa as EsewaServiceType;
