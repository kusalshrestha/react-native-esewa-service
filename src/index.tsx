import { NativeModules } from 'react-native';

type Options = "test" | "live" | "local";

type EsewaServiceType = {
  init(clientId: string, secret: string, environment: Options): void;
  makePayment(price: string, productName: string, productId: string, callbackUrl: string): void;
};

const { Esewa } = NativeModules;

export default Esewa as EsewaServiceType;
