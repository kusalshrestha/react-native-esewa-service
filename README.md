# react-native-esewa-service

This is an unofficial esewa service integration for react native projects. However it has less issues comparitive to existing esewa react native libraries.
TODO: ios integration

## Installation

```sh
yarn add react-native-esewa-service
```

## Usage

```js
import Esewa from "react-native-esewa-service";

// ...

Esewa.init(CLIENT_ID, SECRET, ENVIRONMENT)
Esewa.makePayment(PRICE, PRODUCT_NAME, PRODUCT_ID, CALLBACK_URL)

```

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT
