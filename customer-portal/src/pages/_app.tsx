import store from "app/store";
import Alert from "components/core/Alert";
import type { AppProps } from "next/app";
import { Provider } from "react-redux";
import "../styles/globals.css";

const MyApp = ({ Component, pageProps }: AppProps) => {
  return (
    <Provider store={store}>
      <div className="app-container bg-light">
        <div className="p-4">
          <div className="container">
            <Alert />
            <Component {...pageProps} />
          </div>
        </div>
      </div>
    </Provider>
  );
};

export default MyApp;
