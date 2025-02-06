import "@testing-library/jest-dom";
import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import CreatePostPage from "../../src/app/create-post/page";
import { useRouter } from "next/navigation";

// Mocks
jest.mock("next/navigation", () => ({
    useRouter: jest.fn(),
}));

describe("CreatePostPage", () => {
    let mockPush: jest.Mock<void, [string]>;

    beforeEach(() => {
        // Reset mocks before each test
        mockPush = jest.fn();
        (useRouter as jest.MockedFunction<typeof useRouter>).mockReturnValue({
            push: mockPush,
        } as any);

        // Mocking global fetch for the tests
        global.fetch = jest.fn();
    });

    afterEach(() => {
        jest.clearAllMocks(); // Clears mock data after each test to ensure no leakage between tests
    });

    it("renders input, editor, and publish button", () => {
        render(<CreatePostPage />);

        // Check if the elements are rendered correctly
        expect(screen.getByPlaceholderText("Title")).toBeInTheDocument();
        expect(screen.getByPlaceholderText("Write your story...")).toBeInTheDocument();
        expect(screen.getByRole("button", { name: "Publish" })).toBeInTheDocument();
    });

    it("shows an error message when trying to publish with empty fields", async () => {
        render(<CreatePostPage />);
        
        // Simulating click without filling in the inputs
        fireEvent.click(screen.getByRole("button", { name: "Publish" }));

        // Wait and check if the error message appears
        await waitFor(() => {
            expect(screen.getByText("Title and content cannot be empty")).toBeInTheDocument();
        });
    });

    it("publishes a post successfully", async () => {
        // Mocking a successful response for the fetch request
        (global.fetch as jest.Mock).mockResolvedValueOnce({
            ok: true,
            json: () => Promise.resolve({ postId: "123" }),
        });

        render(<CreatePostPage />);
        
        // Filling the form fields
        fireEvent.change(screen.getByPlaceholderText("Title"), { target: { value: "Test Title" } });
        fireEvent.change(screen.getByPlaceholderText("Write your story..."), { target: { value: "Test Content" } });

        // Simulating button click to publish
        fireEvent.click(screen.getByRole("button", { name: "Publish" }));

        // Waiting for the success message and the redirection
        await waitFor(() => {
            expect(screen.getByText("Post saved successfully!")).toBeInTheDocument();
            expect(mockPush).toHaveBeenCalledWith("/posts/123");
        });
    });

    it("handles API error response", async () => {
        // Mocking an error response for the fetch request
        (global.fetch as jest.Mock).mockResolvedValueOnce({
            ok: false,
            json: () => Promise.resolve({ message: "Failed to publish post" }),
        });

        render(<CreatePostPage />);
        
        // Filling the form fields
        fireEvent.change(screen.getByPlaceholderText("Title"), { target: { value: "Test Title" } });
        fireEvent.change(screen.getByPlaceholderText("Write your story..."), { target: { value: "Test Content" } });

        // Simulating button click to publish
        fireEvent.click(screen.getByRole("button", { name: "Publish" }));

        // Waiting for the error message to appear
        await waitFor(() => {
            expect(screen.getByText("Error: Failed to publish post")).toBeInTheDocument();
        });
    });
});
