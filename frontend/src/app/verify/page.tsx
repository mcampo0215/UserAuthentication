"use client"
import { Text } from "lucide-react"
import { Button } from "@/components/ui/button";
import { Card, CardDescription, CardHeader, CardTitle, CardFooter } from "@/components/ui/card";
import {useRouter} from "next/navigation";

export default function VerifyPage() {
    const router = useRouter();

    const handleResend = async () => {
        const email = localStorage.getItem("pendingEmail");
        try {
        const res = await fetch("http://localhost:8080/api/auth/resend-verification", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                email,
            }),
        });
        const data = await res.text();
        alert(data);
    } catch(err) {
        console.error("Failed to resend:", err);
        alert("Failed to resend verification link.")
    }
}

    const handleGoToLogin = async () => {
        const email = localStorage.getItem("pendingEmail");
        try {
            const res = await fetch(`http://localhost:8080/api/auth/is-verified?email=${email}`);
            const isVerified = await res.json();

            if(isVerified) {
                router.push("/login");
            }
            else {
                alert("You still need to verify your email.");
            }
        } catch(err) {
            console.error("Error checking verification", err);
            alert("Something went wrong");
        }
    };

    return (
        <div className = "flex items-center justify-center min-h-screen">
            <Card className = "w-[600px]">
                <CardHeader>
                    <CardTitle className = "text-center font-serif">Verify Your Account</CardTitle>
                    <CardDescription className = "text-center">
                        Use the verification link sent to your email.
                    </CardDescription>
                </CardHeader>
                <CardFooter className = "flex flex-col space-y-4 items-center">
                    <Button onClick = {handleResend}>Resend Verification</Button>
                    <Button variant = "secondary" onClick = {handleGoToLogin}>Already Verified? Log In</Button>
                </CardFooter>
            </Card>
        </div>
    );
}