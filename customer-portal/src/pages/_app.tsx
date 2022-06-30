import store from "app/store";
import Layout from "layout/Layout";
import { Provider } from "react-redux";
import { AppPropsWithLayout } from "types/layout";
import "../styles/globals.css";

const App = ({ Component, pageProps }: AppPropsWithLayout) => {
  // Use the layout defined at the page level, if available
  const getLayout = Component.getLayout ?? (page => <Layout>{page}</Layout>);

  return (
    <Provider store={store}>{getLayout(<Component {...pageProps} />)}</Provider>
  );
};

export default App;
