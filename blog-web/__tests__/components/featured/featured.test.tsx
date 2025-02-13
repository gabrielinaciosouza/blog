import React from 'react';
import { render, screen } from '@testing-library/react';
import Featured from '@/components/featured/Featured';

describe('Featured', () => {
  it('should render the title', () => {
    render(<Featured />);
    expect(screen.getByText((content, element) => {
      return element?.textContent === "Hey, I'm Gabriel! Unraveling Code, Stories & Big Ideas!🚀";
    })).toBeInTheDocument();
  });

  it('should render the post title', () => {
    render(<Featured />);
    expect(screen.getByText("🎯 Tech, Logic & Creativity - Why I Love Engineering")).toBeInTheDocument();
  });

  it('should render the post description', () => {
    render(<Featured />);
    expect(screen.getByText("Every bug has a lesson, every project has a story. Join me as I navigate the ever-evolving world of software engineering!")).toBeInTheDocument();
  });

  it('should render the Blog button', () => {
    render(<Featured />);
    expect(screen.getByText("Blog")).toBeInTheDocument();
  });

  it('should render the About Me button', () => {
    render(<Featured />);
    expect(screen.getByText("About Me")).toBeInTheDocument();
  });

  it('should render the social media icons', () => {
    render(<Featured />);
    expect(screen.getByAltText("facebook")).toBeInTheDocument();
    expect(screen.getByAltText("instagram")).toBeInTheDocument();
    expect(screen.getByAltText("tiktok")).toBeInTheDocument();
    expect(screen.getByAltText("youtube")).toBeInTheDocument();
  });
});