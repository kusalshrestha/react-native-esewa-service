import * as React from 'react';
import Esewa from 'react-native-esewa-service';
import { StyleSheet, View, Text, TouchableOpacity } from 'react-native';

const MERCHANT_ID = "BxwRAw0WGEUMFUkrJ0w2OEg8KyUgOTU=";
const MERCHANT_SECRET_KEY = "BhwIWQQdHQAXEV0HGBUHBwAKEANLBxMc";

export default function App() {
  React.useEffect(() => {
    return Esewa.init(MERCHANT_ID, MERCHANT_SECRET_KEY, "test");
  }, []);

  const makePayment = () => {
    Esewa.makePayment("500", "book", "1", "")
  }

  return (
    <View style={styles.container}>
      <TouchableOpacity style={styles.button} onPress={makePayment}>
        <Text>Esewa Pay</Text>
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  button: {
    width: 120,
    height: 44,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: 'orange',
  },
});
