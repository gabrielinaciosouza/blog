import { render, screen, fireEvent } from "@testing-library/react";
import ContactPage from "@/app/(public)/contact/page";

describe("ContactPage", () => {
    it("renders heading and contact text", () => {
        render(<ContactPage />);
        expect(screen.getByText("Let's get in touch!")).toBeInTheDocument();
        expect(screen.getByText(/Here you can find my contact/i)).toBeInTheDocument();
    });

    it("renders all contact buttons", () => {
        render(<ContactPage />);
        expect(screen.getByDisplayValue("your@email.com")).toBeInTheDocument();
        expect(screen.getByText("LinkedIn")).toBeInTheDocument();
        expect(screen.getByText("Github")).toBeInTheDocument();
        expect(screen.getByText("Youtube")).toBeInTheDocument();
    });

    it("copies email to clipboard when Copy button is clicked", () => {
        render(<ContactPage />);
        const copyButton = screen.getByText("Copy");
        Object.assign(navigator, {
            clipboard: {
                writeText: jest.fn(),
            },
        });
        fireEvent.click(copyButton);
        expect(navigator.clipboard.writeText).toHaveBeenCalledWith("your@email.com");
    });

    it("selects email input on focus and click", () => {
        render(<ContactPage />);
        const emailInput = screen.getByDisplayValue("your@email.com") as HTMLInputElement;
        // Mock select method
        emailInput.select = jest.fn();
        fireEvent.focus(emailInput);
        expect(emailInput.select).toHaveBeenCalled();
        (emailInput.select as jest.Mock).mockClear();
        fireEvent.click(emailInput);
        expect(emailInput.select).toHaveBeenCalled();
    });
});
