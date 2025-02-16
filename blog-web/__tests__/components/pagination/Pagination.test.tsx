import React from "react";
import { render, screen, fireEvent } from "@testing-library/react";
import Pagination from "@/components/pagination/Pagination";
import { useRouter } from "next/navigation";

jest.mock("next/navigation", () => ({
    useRouter: jest.fn(),
}));

describe("Pagination Component", () => {
    const mockPush = jest.fn();
    beforeEach(() => {
        (useRouter as jest.Mock).mockReturnValue({
            push: mockPush,
        });
        jest.clearAllMocks();
    });

    it("should render Previous and Next buttons", () => {
        render(<Pagination page={1} hasPrev={true} hasNext={true} />);
        expect(screen.getByText("Previous")).toBeInTheDocument();
        expect(screen.getByText("Next")).toBeInTheDocument();
    });

    it("should disable Previous button if hasPrev is false", () => {
        render(<Pagination page={1} hasPrev={false} hasNext={true} />);
        expect(screen.getByText("Previous")).toBeDisabled();
    });

    it("should disable Next button if hasNext is false", () => {
        render(<Pagination page={1} hasPrev={true} hasNext={false} />);
        expect(screen.getByText("Next")).toBeDisabled();
    });

    it("should enable Previous and Next buttons if hasPrev and hasNext are true", () => {
        render(<Pagination page={1} hasPrev={true} hasNext={true} />);
        expect(screen.getByText("Previous")).toBeEnabled();
        expect(screen.getByText("Next")).toBeEnabled();
    });

    it("should call router.push with correct page number when Previous button is clicked", () => {
        render(<Pagination page={2} hasPrev={true} hasNext={true} />);
        fireEvent.click(screen.getByText("Previous"));
        expect(mockPush).toHaveBeenCalledWith("?page=1");
    });

    it("should call router.push with correct page number when Next button is clicked", () => {
        render(<Pagination page={2} hasPrev={true} hasNext={true} />);
        fireEvent.click(screen.getByText("Next"));
        expect(mockPush).toHaveBeenCalledWith("?page=3");
    });
});