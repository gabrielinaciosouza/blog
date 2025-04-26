import React from "react";
import { render, screen } from "@testing-library/react";
import AnimatedText from "@/components/animatedText/AnimatedText";

describe("AnimatedText", () => {
  it("renders children text", () => {
    render(<AnimatedText>Animated Text</AnimatedText>);
    expect(screen.getByText("Animated Text")).toBeInTheDocument();
  });

  it("applies the animated class and custom className", () => {
    render(<AnimatedText className="custom-class">Test</AnimatedText>);
    const span = screen.getByText("Test");
    expect(span.className).toContain("animated");
    expect(span.className).toContain("custom-class");
  });
});
