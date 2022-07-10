interface PasswordResetRequest {
  email: string;
  token: string;
  currentPassword: string;
  newPassword: string;
  passwordConfirm: string;
}

export const initialPasswordResetRequestState: PasswordResetRequest = {
  email: "",
  token: "",
  currentPassword: "",
  newPassword: "",
  passwordConfirm: "",
};

export default PasswordResetRequest;
