import React from "react";
import { render, screen } from "@testing-library/react";
import MarkdownContent from "@/components/MarkdownContent";

describe("MarkdownContent", () => {
    it("renders markdown links and images correctly", () => {
        const markdown = `# Title\n\n[Google](https://google.com)\n\n![Alt text](https://example.com/image.png)`;
        render(<MarkdownContent content={markdown} />);
        // Check heading
        expect(screen.getByText("Title")).toBeInTheDocument();
        // Check link
        const link = screen.getByRole("link", { name: /google/i });
        expect(link).toHaveAttribute("href", "https://google.com");
        // Check image
        const img = screen.getByRole("img", { name: /alt text/i });
        expect(img).toHaveAttribute("src", "/_next/image?url=https%3A%2F%2Fexample.com%2Fimage.png&w=1080&q=75");
    });

    it("renders lists and paragraphs", () => {
        const markdown = `Paragraph text.\n\n- Item 1\n- Item 2`;
        render(<MarkdownContent content={markdown} />);
        expect(screen.getByText("Paragraph text.")).toBeInTheDocument();
        expect(screen.getByText("Item 1")).toBeInTheDocument();
        expect(screen.getByText("Item 2")).toBeInTheDocument();
    });
});
