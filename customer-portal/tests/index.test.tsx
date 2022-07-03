import "@testing-library/jest-dom";
import { render, screen } from "@testing-library/react";
import { initialProductPage } from "models/ProductPage";
import Home from "../src/pages/index";

describe("Home", () => {
  it("renders a heading", () => {
    render(<Home productPage={initialProductPage} />);

    const heading = screen.getByRole("heading", {
      name: /Hello World/i,
    });

    expect(heading).toBeInTheDocument();
  });
});
