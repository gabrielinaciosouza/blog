import React from 'react';
import { render, screen } from '@testing-library/react';
import Navbar from '@/components/navbar/Navbar';

describe('Navbar', () => {

  beforeEach(() => {
    jest.resetModules();
  });

  it('passes isAdmin=false when no cookie or non-admin cookie', async () => {
    jest.mock('next/headers', () => ({
      cookies: async () => ({ get: () => null })
    }));
    const Navbar = (await import('@/components/navbar/Navbar')).default;
    const result = await Navbar();
    expect(result.props.isAdmin).toBe(false);
  });

  it('passes isAdmin=true when cookie contains ADMIN role', async () => {
    jest.mock('next/headers', () => ({
      cookies: async () => ({ get: () => ({ value: JSON.stringify({ role: 'ADMIN' }) }) })
    }));
    const Navbar = (await import('@/components/navbar/Navbar')).default;
    const result = await Navbar();
    expect(result.props.isAdmin).toBe(true);
  });

  it('passes isAdmin=false when cookie contains non-admin role', async () => {
    jest.mock('next/headers', () => ({
      cookies: async () => ({ get: () => ({ value: JSON.stringify({ role: 'USER' }) }) })
    }));
    const Navbar = (await import('@/components/navbar/Navbar')).default;
    const result = await Navbar();
    expect(result.props.isAdmin).toBe(false);
  });
});