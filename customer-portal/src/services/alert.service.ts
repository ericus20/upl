import AlertType from "enums/AlertType";
import { filter, Subject } from "rxjs";

export interface AlertBody {
  id?: string;
  autoClose?: boolean;
  options?: object;
  type?: AlertType;
  message?: string;
  fade?: boolean;
  keepAfterRouteChange?: boolean;
}

const alertSubject = new Subject<AlertBody>();
const defaultId = "default-alert";

// enable subscribing to alerts observable
const onAlert = (id: string = defaultId) => {
  return alertSubject.asObservable().pipe(filter(x => x && x.id === id));
};

// core alert method
const alert = (thisAlert: AlertBody) => {
  alertSubject.next({
    id: thisAlert.id || defaultId,
    autoClose: thisAlert.autoClose === undefined ? true : thisAlert.autoClose,
  });
};

const success = (message: string, options: object) => {
  alert({ ...options, type: AlertType.SUCCESS, message });
};

const info = (message: string, options: object) => {
  alert({ ...options, type: AlertType.INFO, message });
};

const warn = (message: string, options: object) => {
  alert({ ...options, type: AlertType.WARNING, message });
};

const error = (message: string, options: object) => {
  alert({ ...options, type: AlertType.ERROR, message });
};

// clear alerts
const clear = (id: string = defaultId) => {
  alertSubject.next({ id });
};

export const alertService = {
  onAlert,
  success,
  error,
  info,
  warn,
  alert,
  clear,
};
