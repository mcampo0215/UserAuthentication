"use client";
import {
    Card,
    CardContent,
    CardDescription,
    CardFooter,
    CardHeader,
    CardTitle,
  } from "@/components/ui/card"
  import { Label } from "@/components/ui/label"
  import { Button } from "@/components/ui/button"
  import { Input } from "@/components/ui/input"
  import * as React from "react"
import { useState } from "react"
import {useRouter} from "next/navigation";

  export default function logInPage() {
    const router = useRouter();
    const[showPassword, setShowPassword] = useState(false);
    const[form, setForm] = useState({
        username: "",
        password: "",
    });
    const[message, setMessage] = useState("");
    
    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setForm({...form, [e.target.id]: e.target.value});
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        
        try {
            const res = await fetch("http://localhost:8080/api/auth/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    username: form.username,
                    password: form.password,
                }),
            });
            const result = await res.text();

            if(res.ok) {
                alert("Succesfully logged in.");
                router.push("/success");
            }
            else {
                alert(result);
                setMessage(result);
            }
        } catch(err) {
            console.error("Error:", err);
            setMessage("Something went wrong.");
        }
    };


    return (
        <div className = "flex items-center justify-center min-h-screen">
        <Card className = "w-[800px]">
            <CardHeader>
                 <CardTitle className = "font-serif">Log In</CardTitle>
                 <CardDescription className = "font-mono">Welcome to App</CardDescription>
            </CardHeader>

            <form onSubmit = {handleSubmit}>
                <div className = "grid w-full items-center gap-4">
                    <div className = "flex flex-col space-y-1.5">
                        <Label className = "font-mono" htmlFor = "username">Username</Label>
                        <Input id = "username" value = {form.username} onChange = {handleChange} placeholder = "Enter Username"></Input>
                    </div>

                    <div className = "flex flex-col space-y-1.5">
                        <Label className = "font-mono" htmlFor = "password">Password</Label>
                        <div className = "relative">
                        <Input className = "pr-10" type = {showPassword ? "text" : "password"}id = "password" value = {form.password} onChange = {handleChange} placeholder = "Enter Password"></Input>
                        <button type = "button" className = "absolute right-2 top-1/2 -translate-y-1/2 text-black dark:text-white" onClick = {() => {setShowPassword((prev) => !prev)}}>{showPassword ? <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" stroke-linecap="round" stroke-linejoin="round" className="lucide lucide-eye-off-icon lucide-eye-off"><path d="M10.733 5.076a10.744 10.744 0 0 1 11.205 6.575 1 1 0 0 1 0 .696 10.747 10.747 0 0 1-1.444 2.49"/><path d="M14.084 14.158a3 3 0 0 1-4.242-4.242"/><path d="M17.479 17.499a10.75 10.75 0 0 1-15.417-5.151 1 1 0 0 1 0-.696 10.75 10.75 0 0 1 4.446-5.143"/><path d="m2 2 20 20"/></svg>: <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" className="lucide lucide-eye-icon lucide-eye"><path d="M2.062 12.348a1 1 0 0 1 0-.696 10.75 10.75 0 0 1 19.876 0 1 1 0 0 1 0 .696 10.75 10.75 0 0 1-19.876 0"/><circle cx="12" cy="12" r="3"/></svg>}</button>
                        </div>
                    </div>
                </div>
                <CardFooter className = "flex justify-center">
                    <Button type = "submit" className = "font-serif transition-transform duration-100 active:scale-95">Log In</Button>
                </CardFooter>
            </form>
            </Card>
        </div>

    )
  }