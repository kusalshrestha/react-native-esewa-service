import { NativeModules } from 'react-native';

type EsewaServiceType = {
  multiply(a: number, b: number): Promise<number>;
};

const { EsewaService } = NativeModules;

export default EsewaService as EsewaServiceType;
