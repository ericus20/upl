import { useAppSelector } from "app/hooks";
import { selectAuth } from "app/slices/auth";
import dynamic from "next/dynamic";
import React from "react";

// Dynamically import the AuthGuard component.
const AuthGuard = dynamic<{
  children: React.ReactNode;
}>(() => import("components/layout/AuthGuard"));

const Secured = () => {
  const { principal } = useAppSelector(selectAuth);
  return (
    <AuthGuard>
      <section className=" text-center m-auto mt-20 justify-center items-center">
        <h2>Welcome {principal.username}, You are in a secured spot!</h2>
      </section>
    </AuthGuard>
  );
};

export default Secured;
