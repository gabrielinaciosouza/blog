import { render, screen, fireEvent } from "@testing-library/react";
import { userEvent } from "@testing-library/user-event"
import CreatePostPage from "../../src/app/create-post/page";
import { describe, it, expect, vi } from "vitest";
import React from "react";

vi.mock("next/navigation", () => ({
    useRouter: () => ({ push: vi.fn() }),
}));

describe("CreatePostPage", () => {
    it("renders input, editor and publish button", () => {
        render(<CreatePostPage />);

        expect(screen.getByPlaceholderText("Title")).toBeInTheDocument();
        expect(document.querySelector("[data-placeholder='Write your story...']")).toBeInTheDocument();
        expect(screen.getByRole("button", { name: /publish/i })).toBeInTheDocument();
    });

    it("displays error when publishing with empty fields", async () => {
        render(<CreatePostPage />);

        fireEvent.click(screen.getByRole("button", { name: /publish/i }));

        expect(await screen.findByText("Title and content cannot be empty")).toBeInTheDocument();
    });

    it("shows success message after successful post", async () => {
        global.fetch = vi.fn(() =>
            Promise.resolve({
                ok: true,
                json: () => Promise.resolve({ postId: "123" }),
            })
        ) as never;

        render(<CreatePostPage />);

        fireEvent.change(screen.getByPlaceholderText("Title"), { target: { value: "My Post" } });
        await userEvent.type(document.querySelector("[data-placeholder='Write your story...']") as Element, "This is a test post")

        fireEvent.click(screen.getByRole("button", { name: /publish/i }));

        expect(await screen.findByText("Post saved successfully!")).toBeInTheDocument();
    });
});
