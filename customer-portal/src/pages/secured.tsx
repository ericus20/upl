import { useAppSelector } from "app/hooks";
import { selectAuth } from "app/slices/auth";
import axiosInstance from "libs/axios";
import dynamic from "next/dynamic";
import React from "react";

// Dynamically import the AuthGuard component.
const AuthGuard = dynamic<{
  children: React.ReactNode;
}>(() => import("components/layout/AuthGuard"));

const Secured = () => {
  const { principal } = useAppSelector(selectAuth);

  const enableUser = async (event: React.MouseEvent<HTMLButtonElement>) => {
    event.preventDefault();

    const status = await axiosInstance.put<string>(
      `/api/v1/users/${principal.publicId}/enable`
    );

    console.log("status", await status);
  };

  return (
    <AuthGuard>
      <section className=" text-center m-auto mt-20 justify-center items-center">
        <h2>Welcome {principal.username}, You are in a secured spot!</h2>

        <button type="button" onClick={enableUser}>
          Enable me
        </button>
      </section>
    </AuthGuard>
  );
};

export default Secured;