import store from "app/store";
import Layout from "components/layout/Layout";
import type { AppProps } from "next/app";
import { Provider } from "react-redux";
import "../styles/globals.css";

const MyApp = ({ Component, pageProps }: AppProps) => {
  return (
    <Provider store={store}>
      <Layout>
        <Component {...pageProps} />
      </Layout>
    </Provider>
  );
};

export default MyApp;
