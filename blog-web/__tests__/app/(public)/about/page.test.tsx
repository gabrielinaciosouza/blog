import { render, screen } from "@testing-library/react";
import AboutPage from "@/app/(public)/about/page";

describe("AboutPage", () => {
    it("renders the title", () => {
        render(<AboutPage />);
        expect(screen.getByText(/About me/i)).toBeInTheDocument();
    });

    it("renders the profile image", () => {
        render(<AboutPage />);
        const img = screen.getByAltText(/Gabriel Inacio profile/i);
        expect(img).toBeInTheDocument();
        expect(img).toHaveClass("rounded-full");
    });

    it("renders highlighted keywords", () => {
        render(<AboutPage />);
        expect(screen.getByText(/Gabriel Inacio/i)).toBeInTheDocument();
        expect(screen.getByText(/software engineer/i)).toBeInTheDocument();
        expect(screen.getByText(/clean code/i)).toBeInTheDocument();
        expect(screen.getByText(/scalable systems/i)).toBeInTheDocument();
        expect(screen.getByText(/performance-first thinking/i)).toBeInTheDocument();
    });

    it("renders all paragraphs", () => {
        render(<AboutPage />);
        expect(screen.getByText(/banking systems/i)).toBeInTheDocument();
        expect(screen.getByText(/powerline electrician/i)).toBeInTheDocument();
        expect(screen.getByText(/Poland/i)).toBeInTheDocument();
        expect(screen.getByText(/GPC Global/i)).toBeInTheDocument();
        expect(screen.getByText(/system architecture/i)).toBeInTheDocument();
        expect(screen.getByText(/cloud-native development/i)).toBeInTheDocument();
        expect(screen.getByText(/thoughtful, real-world software discussions/i)).toBeInTheDocument();
    });
});
