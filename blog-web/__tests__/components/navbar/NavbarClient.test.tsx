import React from "react";
import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import NavbarClient from "@/components/navbar/NavbarClient";

describe("NavbarClient", () => {
    beforeAll(() => {
        // Mock window.location.href to prevent navigation errors in jsdom
        Object.defineProperty(window, 'location', {
            configurable: true,
            value: { href: '', assign: jest.fn(), replace: jest.fn() }
        });
    });
    it("renders logo and title", () => {
        render(<NavbarClient isAdmin={false} />);
        expect(screen.getByAltText(/logo/i)).toBeInTheDocument();
        expect(screen.getByText(/Gabriel's Blog/i)).toBeInTheDocument();
    });

    it("shows admin button only for admins (desktop)", () => {
        render(<NavbarClient isAdmin={true} />);
        expect(screen.getAllByText(/Admin/i)[0]).toBeInTheDocument();
    });

    it("does not show admin button for non-admins (desktop)", () => {
        render(<NavbarClient isAdmin={false} />);
        expect(screen.queryByText(/Admin/i)).not.toBeInTheDocument();
    });

    it("opens and closes mobile menu", async () => {
        render(<NavbarClient isAdmin={false} />);
        const menuButton = screen.getByText(/Menu/i);
        fireEvent.click(menuButton);
        // The second Homepage button is in the mobile menu
        const homepageButtons = screen.getAllByText(/Homepage/i);
        expect(homepageButtons[1]).toBeInTheDocument();
        fireEvent.click(homepageButtons[1]);
        // Wait for menu to close by checking for the absence of the Sheet dialog
        await waitFor(() => {
            expect(document.querySelector('[role="dialog"]')).toBeNull();
        });
    });

    it("shows admin button in mobile menu only for admins", () => {
        render(<NavbarClient isAdmin={true} />);
        fireEvent.click(screen.getByText(/Menu/i));
        expect(screen.getAllByText(/Admin/i)[0]).toBeInTheDocument();
    });

    it("does not show admin button in mobile menu for non-admins", () => {
        render(<NavbarClient isAdmin={false} />);
        fireEvent.click(screen.getByText(/Menu/i));
        expect(screen.queryByText(/Admin/i)).not.toBeInTheDocument();
    });

    it("dismisses mobile menu on any button click", async () => {
        render(<NavbarClient isAdmin={true} />);
        fireEvent.click(screen.getByText(/Menu/i));
        const contactButtons = screen.getAllByText(/Contact/i);
        fireEvent.click(contactButtons[1]);
        // Wait for menu to close by checking for the absence of the Sheet dialog
        await waitFor(() => {
            expect(document.querySelector('[role="dialog"]')).toBeNull();
        });
    });
});
