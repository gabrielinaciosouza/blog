import React from "react";
import { render, screen } from "@testing-library/react";
import AnimatedImage from "@/components/animatedImage/AnimatedImage";

jest.mock("next/image", () => ({
  __esModule: true,
  default: (props: any) => <img {...props} />,
}));

describe("AnimatedImage", () => {
  it("renders with correct src and alt", () => {
    render(
      <AnimatedImage src="/test.png" alt="test image" width={100} height={100} />
    );
    const img = screen.getByAltText("test image");
    expect(img).toBeInTheDocument();
    expect(img).toHaveAttribute("src", "/test.png");
  });

  it("applies the animated class and custom className", () => {
    render(
      <AnimatedImage src="/test.png" alt="test image" width={100} height={100} className="custom-class" />
    );
    const img = screen.getByAltText("test image");
    expect(img.className).toContain("animated");
    expect(img.className).toContain("custom-class");
  });
});
